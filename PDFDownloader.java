package com.array.medium;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class PDFDownloader {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://courses.genzcareer.org/complete-interview-prepration-id23242555de32f5/");
        URLConnection urlConnection = url.openConnection();

// Faster: use BufferedReader + 8KB buffer
        BufferedReader in = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream()), 8192);

        String inputLine;
        String inputLine;
        AtomicInteger i= new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        while ((inputLine = in.readLine()) != null) {
            String finalInputLine = inputLine;
            executorService.submit( () -> {
                if (finalInputLine.contains("<a href")) {
                    if (i.get() > 0) {
                        String pdfUrl = finalInputLine.substring(finalInputLine.indexOf("\""));
                        // System.out.println(pdfUrl.split(" ")[0]);
                        int start = finalInputLine.indexOf("href=\"") + 6;
                        int end = finalInputLine.indexOf("\"", start);
                        String filename = finalInputLine.substring(start, end);
                        System.out.println("filename " + ":" + filename);
                        URL pdflinks = null;
                        ReadableByteChannel readableByteChannel = null;
                        FileOutputStream fp = null;

                        try {
                            pdflinks = new URL(filename);
                            readableByteChannel = Channels.newChannel(pdflinks.openStream());
                            fp = new FileOutputStream(filename.substring(filename.lastIndexOf("/") + 1));
                            fp.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    i.getAndIncrement();
                    String checkedMark = "\u2713";
                    System.out.println(checkedMark + " Downloaded: " + "File 1");
                }
            });

        }
        System.out.println(i.get());
        in.close(); 

    }
}
