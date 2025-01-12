package com.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        ServerSocket ss = new ServerSocket(8080);
        while(true) {
            Socket s = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String firstLine = in.readLine();
            System.out.println(firstLine);
            String method = firstLine.split(" ")[0];
            String resource = firstLine.split(" ")[1];
            String version = firstLine.split(" ")[2];

            String header;
            do {
                header = in.readLine();
                System.out.println(header);
            }while(!header.isEmpty());
            System.out.println("richiesta terminata");

            /*switch (resource) {
                case "/file.txt":
                    responseBody = "file.txt\n";
                    out.writeBytes("HTTP1.1 200 OK\n");
                    out.writeBytes("Content-Type: text/plain\n");
                    out.writeBytes("Content-Lenght: " + responseBody.length() + "\n");
                    out.writeBytes("\n");
                    out.writeBytes(responseBody);
                break;
                case "/pagina.html":
                    responseBody = "pagina.html";
                    out.writeBytes("HTTP1.1 200 OK\n");
                    out.writeBytes("Content-Type: text/html\n");
                    out.writeBytes("Content-Lenght: " + responseBody.length() + "\n");
                    out.writeBytes("\n");
                    out.writeBytes(responseBody);
                break;
                case "/index.html":
                    responseBody = "index.html";
                    out.writeBytes("HTTP1.1 200 OK\n");
                    out.writeBytes("Content-Type: text/html\n");
                    out.writeBytes("Content-Lenght: " + responseBody.length() + "\n");
                    out.writeBytes("\n");
                    out.writeBytes(responseBody);
                break;
                default:
                    responseBody ="404 not found";
                    out.writeBytes("HTTP1.1 200 OK\n");
                    out.writeBytes("Content-Type: text/html\n");
                    out.writeBytes("Content-Lenght: " + responseBody.length() + "\n");
                    out.writeBytes("\n");
                    out.writeBytes(responseBody);
                break;
                }*/
               File file = new File("../../../../htdocs/index.html");
                System.out.println("Resolved path: " + file.getAbsolutePath()); // Debugging

                if (file.exists()) {
                    out.writeBytes("HTTP/1.1 200 OK\n");
                    out.writeBytes("Content-Length: " + file.length() + "\n"); // Fixed typo
                    out.writeBytes("Content-Type: text/html\n");
                    out.writeBytes("\n");
    
                try (InputStream input = new FileInputStream(file)) { // Use try-with-resources
                    byte[] buf = new byte[8192];
                    int n;
                    while ((n = input.read(buf)) != -1) {
                    out.write(buf, 0, n);
                }    
            }
        } else {
            out.writeBytes("HTTP/1.1 404 Not Found\n");
            out.writeBytes("Content-Type: text/plain\n\n");
            out.writeBytes("File not found.");
        }
     }
    }
