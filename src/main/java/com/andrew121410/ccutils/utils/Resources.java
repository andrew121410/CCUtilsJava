package com.andrew121410.ccutils.utils;

import com.andrew121410.ccutils.CCUtils;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Resources {

    @SneakyThrows
    public static List<String> getAllResources() {
        List<String> list = new ArrayList<>();
        CodeSource src = CCUtils.class.getProtectionDomain().getCodeSource();
        if (src != null) {
            URL jar = src.getLocation();
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            while (true) {
                ZipEntry zipEntry = zip.getNextEntry();
                if (zipEntry == null)
                    break;
                list.add(zipEntry.getName());
            }
        } else {
            throw new NullPointerException("CodeSource was null lol?");
        }
        return list;
    }

    @SneakyThrows
    public static String readFromInputStream(InputStream inputStream) {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    @SneakyThrows
    public static String getResourcesInJarAsText(String path) {
        InputStream inputStream = CCUtils.class.getResourceAsStream(path);
        return readFromInputStream(inputStream);
    }
}
