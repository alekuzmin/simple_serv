package ru.alexkuz.simple.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());

    static {
        try {
            Handler handler = new FileHandler("server.log");
            handler.setLevel(Level.ALL);
            LOGGER.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            LOGGER.info("Server started...");
            ServerSocket serverSocket = new ServerSocket(8080);
            ExecutorService es = Executors.newFixedThreadPool(10);
            for (int i = 0; i < 3; i++) {
                LOGGER.info("Thread #" + i + " started.");
                es.submit(new Thread(() -> {
                    try {
                        while (true) {
                            Socket clientSocket = serverSocket.accept();
                            InputStreamReader input = new InputStreamReader(clientSocket.getInputStream());
                            Thread.sleep(1000);
                            StringBuilder sb = new StringBuilder();
                            while (input.ready()) {
                                sb.append((char) input.read());
                            }
                            LOGGER.info("Read request");
                            System.out.println(sb);


                            OutputStreamWriter out = new OutputStreamWriter(clientSocket.getOutputStream());
                            String response = "HTTP/1.1 200 OK\n" +
                                    "Cache-Control: no-cache\n" +
                                    "Connection: close\n" +
                                    "Content-Type: application/json\n\n" +
                                    "{\"ok\": \"" + sb.toString() + "\"}";
                            //Thread.sleep(100);
                            out.write(response);
                            out.flush();
                            LOGGER.info("Message sent");
                            input.close();
                            clientSocket.close();
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }));
            }
            Thread.sleep(10000000);
            serverSocket.close();
            LOGGER.info("Server shutdown.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
