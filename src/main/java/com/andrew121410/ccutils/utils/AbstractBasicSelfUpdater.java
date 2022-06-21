package com.andrew121410.ccutils.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class AbstractBasicSelfUpdater {

    private final String currentJarLocation;
    private final String urlOfJar;
    private final String urlOfHash;

    private boolean updatedAlready = false;

    public AbstractBasicSelfUpdater(Class<?> aClass, String urlOfJar, String urlOfHash) {
        this(aClass.getProtectionDomain().getCodeSource().getLocation().getFile(), urlOfJar, urlOfHash);
    }

    public AbstractBasicSelfUpdater(String currentJarLocation, String urlOfJar, String urlOfHash) {
        this.currentJarLocation = currentJarLocation;
        this.urlOfJar = urlOfJar;
        this.urlOfHash = urlOfHash;
    }

    public boolean shouldUpdate() {
        String hashOfCurrentJar = getHashOfCurrentJar();
        String hashFromRemote = getHashFromRemote();

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
            FileUtils.download(this.urlOfJar, tempFile);

            // Verify the hash of the downloaded file before we replace the current one
            String hashFromRemote = getHashFromRemote();
            String hashOfDownloadedFile = getHashOfFile(tempFile);
            if (!hashOfDownloadedFile.equals(hashFromRemote)) {
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
            FileUtils.download(this.urlOfHash, hashFile);

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
