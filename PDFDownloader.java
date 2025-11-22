package com.array.medium;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalTime;

public class PDFDownloader {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://courses.genzcareer.org/complete-interview-prepration-id23242555de32f5/");
        URLConnection urlConnection = url.openConnection();

// Faster: use BufferedReader + 8KB buffer
        BufferedReader in = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream()), 8192);

        String inputLine;
        int i = 0;
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
        while ((inputLine = in.readLine()) != null) {

            if (inputLine.contains("<a href")) {

                if (i > 7) {

                    // Faster & safe extraction of href
                    int start = inputLine.indexOf("href=\"");
                    if (start == -1) continue;

                    start += 6; // skip href="
                    int end = inputLine.indexOf("\"", start);
                    if (end == -1) continue;

                    String pdfUrl = inputLine.substring(start, end);

                    System.out.println("filename: " + pdfUrl);

                    // Extract file name
                    String filename = pdfUrl.substring(pdfUrl.lastIndexOf("/") + 1);

                    // Faster I/O: use buffered streams
                    try (InputStream is = new BufferedInputStream(new URL(pdfUrl).openStream());
                         FileOutputStream fos = new FileOutputStream(filename);
                         BufferedOutputStream bos = new BufferedOutputStream(fos, 8192)) {

                        byte[] buffer = new byte[8192];
                        int bytesRead;

                        while ((bytesRead = is.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }

                        System.out.println("✔ Downloaded: " + filename);

                    } catch (Exception e) {
                        System.out.println("✘ Failed: " + pdfUrl);
                        e.printStackTrace();
                    }
                }

                i++;
            }
        }
        System.out.println(LocalTime.now());
        System.out.println("Total links found: " + i);
        in.close();

    }
}
