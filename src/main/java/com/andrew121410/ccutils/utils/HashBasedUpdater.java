package com.andrew121410.ccutils.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class HashBasedUpdater {

    private final File currentFileLocation;
    private final String urlOfJar;
    private final String urlOfHash;
    private final File optionalNewLocation;

    private boolean updatedAlready = false;

    public HashBasedUpdater(Class<?> aClass, String urlOfJar, String urlOfHash) {
        this(new File(aClass.getProtectionDomain().getCodeSource().getLocation().getFile()), urlOfJar, urlOfHash, null);
    }

    public HashBasedUpdater(File currentFileLocation, String urlOfJar, String urlOfHash) {
        this(currentFileLocation, urlOfJar, urlOfHash, null);
    }

    public HashBasedUpdater(File currentFileLocation, String urlOfJar, String urlOfHash, File optionalNewLocation) {
        this.currentFileLocation = currentFileLocation;
        this.urlOfJar = urlOfJar;
        this.urlOfHash = urlOfHash;
        this.optionalNewLocation = optionalNewLocation;
    }

    public boolean shouldUpdate() {
        String hashOfCurrentFile = getHashOfCurrentJar();
        String hashFromRemote = getHashFromRemote();

        return !hashOfCurrentFile.equals(hashFromRemote);
    }

    public String update() {
        if (this.updatedAlready)
            return "An update is already in progress, or you have already updated, and need to restart your server.";
        this.updatedAlready = true;

        String tempDirectory = System.getProperty("java.io.tmpdir");
        String nameOfCurrentFileLocation = currentFileLocation.getName();

        File tempFile = new File(tempDirectory, "ccutils-" + nameOfCurrentFileLocation + "." + UUID.randomUUID() + ".tmp");
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
            Path to = (optionalNewLocation != null) ? optionalNewLocation.toPath() : currentFileLocation.toPath();
            java.nio.file.Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            this.updatedAlready = false;
            e.printStackTrace();
            return "Failed to update " + currentFileLocation.getName();
        }

        return "Updated to latest version!";
    }

    public String getHashFromRemote() {
        String tempDirectory = System.getProperty("java.io.tmpdir");
        File hashFile = new File(tempDirectory, "ccutils-hash-" + UUID.randomUUID() + ".txt");
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
        return getHashOfFile(currentFileLocation);
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
