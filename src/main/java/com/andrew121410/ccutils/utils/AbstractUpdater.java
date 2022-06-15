package com.andrew121410.ccutils.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class AbstractUpdater {

    private final String currentJarLocation;
    private final String urlOfJar;
    private final String urlOfHash;

    private String cacheOfHashFromRemote = null;
    private boolean updatedAlready = false;

    public AbstractUpdater(Class<?> aClass, String urlOfJar, String urlOfHash) {
        this(aClass.getProtectionDomain().getCodeSource().getLocation().getFile(), urlOfJar, urlOfHash);
    }

    public AbstractUpdater(String currentJarLocation, String urlOfJar, String urlOfHash) {
        this.currentJarLocation = currentJarLocation;
        this.urlOfJar = urlOfJar;
        this.urlOfHash = urlOfHash;
    }

    public boolean shouldUpdate(boolean shouldCacheRemoteHash) {
        String hashOfCurrentJar = getHashOfCurrentJar();
        String hashFromRemote = getHashFromRemote();

        if (shouldCacheRemoteHash) this.cacheOfHashFromRemote = hashFromRemote;

        return !hashOfCurrentJar.equals(hashFromRemote);
    }

    public String update() {
        if (this.updatedAlready)
            return "An update is already in progress, or you have already updated, and need to restart your server.";
        this.updatedAlready = true;

        String tempDirectory = System.getProperty("java.io.tmpdir");
        File fileOfCurrentJar = new File(this.currentJarLocation);

        File tempFile = new File(tempDirectory, fileOfCurrentJar.getName());
        try {
            // Download the file from the URL.
            URL website = new URL(this.urlOfJar);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            if (this.cacheOfHashFromRemote == null) {
                this.cacheOfHashFromRemote = getHashFromRemote();
            }

            // Verify the hash of the downloaded file before we replace the current one
            String hashOfDownloadedFile = getHashOfFile(tempFile);
            if (!hashOfDownloadedFile.equals(this.cacheOfHashFromRemote)) {
                this.updatedAlready = false;
                return "Hash of downloaded file does not match hash from remote. Aborting update.";
            }

            Path from = tempFile.toPath();
            Path to = fileOfCurrentJar.toPath();
            java.nio.file.Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            this.updatedAlready = false;
            return "Failed to update " + fileOfCurrentJar.getName();
        }

        return "Updated to latest version!";
    }

    public String getHashFromRemote() {
        String tempDirectory = System.getProperty("java.io.tmpdir");
        File hashFile = new File(tempDirectory, "ccutils-" + UUID.randomUUID() + ".txt");
        try {
            // Download the file from the URL.
            ReadableByteChannel readChannel = Channels.newChannel(new URL(this.urlOfHash).openStream());
            FileOutputStream fileOS = new FileOutputStream(hashFile);
            FileChannel writeChannel = fileOS.getChannel();
            writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE);

            // Read the hash from the file
            String data = new String(java.nio.file.Files.readAllBytes(hashFile.toPath()));
            String[] args = data.split(" ");
            return args[0];
        } catch (Exception e) {
            return null;
        }
    }

    public String getHashOfCurrentJar() {
        File locationOfJar = new File(this.currentJarLocation);
        return getHashOfFile(locationOfJar);
    }

    public String getHashOfFile(File file) {
        try {
            HashCode hash = Files.asByteSource(file).hash(Hashing.sha256());
            return hash.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
