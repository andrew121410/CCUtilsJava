package com.andrew121410.ccutils.utils;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FileUtils {
    public static void download(String urlStr, File file) throws IOException {
        URL url = new URL(urlStr);
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        fileOutputStream.close();
        readableByteChannel.close();
    }

    public static void download(String urlStr, String filePath) throws IOException {
        download(urlStr, new File(filePath));
    }

    @SneakyThrows
    public static File getTheFolderTheJarIsIn() {
        return new File(FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
    }
}
