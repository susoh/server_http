package com.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(80);
        System.out.println("Server aperto su http://127.0.0.1/");

        while (true) {
            Socket s = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String primaRiga = in.readLine();
            if (primaRiga == null) {
                s.close();
                continue;
            }

            System.out.println("Richiesta: " + primaRiga);

            String[] parti = primaRiga.split(" ");
            if (parti.length < 2) {
                out.writeBytes("HTTP/1.1 400 Bad Request\nContent-Type: text/plain\n\nRichiesta malformata.\n");
                s.close();
                continue;
            }

            String metodo = parti[0];
            String risorsa = parti[1];

            if (!metodo.equals("GET")) {
                out.writeBytes("HTTP/1.1 405 Method Not Allowed\nContent-Type: text/plain\n\nMetodo non supportato.\n");
                s.close();
                continue;
            }

            if (risorsa.equals("/")) {
                risorsa = "/index.html";
            }

            File file = new File("htdocs", risorsa.substring(1));

            if (file.exists()) {
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
                out.writeBytes("HTTP/1.1 404 Not Found\nContent-Type: text/plain\n\nFile non trovato.\n");
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
        return "text/plain";
    }
}
