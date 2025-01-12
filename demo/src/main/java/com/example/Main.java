package com.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);

        while (true) {
            Socket s = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String primaRiga = in.readLine();
            if (primaRiga == null) {
                s.close();
                continue;
            }

            String[] parti = primaRiga.split(" ");
            String metodo = parti[0];
            String risorsa = parti[1];
            if (!metodo.equals("GET")) {
                out.writeBytes("HTTP/1.1 405 Method Not Allowed\nContent-Type: text/plain\n\nMetodo non supportato.");
                s.close();
                continue;
            }

            if (risorsa.equals("/")) {
                risorsa = "/index.html";
            }

            File file = new File("../../../../htdocs" + risorsa);

            if (file.exists() && file.isFile()) {
                String contentType = getContentType(risorsa);
                out.writeBytes("HTTP/1.1 200 OK\n");
                out.writeBytes("Content-Length: " + file.length() + "\n");
                out.writeBytes("Content-Type: " + contentType + "\n\n");

                try (InputStream input = new FileInputStream(file)) {
                    byte[] buffer = new byte[8192];
                    int n;
                    while ((n = input.read(buffer)) != -1) {
                        out.write(buffer, 0, n);
                    }
                }
            } else {
                out.writeBytes("HTTP/1.1 404 Not Found\nContent-Type: text/plain\n\nFile non trovato.");
            }

            s.close();
        }
    }

    private static String getContentType(String risorsa) {
        if (risorsa.endsWith(".html")) return "text/html";
        if (risorsa.endsWith(".css")) return "text/css";
        if (risorsa.endsWith(".js")) return "application/javascript";
        if (risorsa.endsWith(".png")) return "image/png";
        if (risorsa.endsWith(".jpg") || risorsa.endsWith(".jpeg")) return "image/jpeg";
        return "application/octet-stream";
    }
}
