package ru.alexkuz.simple.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try{
            Socket clientSocket = new Socket("localhost", 8080);
            OutputStream out = clientSocket.getOutputStream();
            out.write("Djopa".getBytes());
            out.flush();

            BufferedReader bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bf.lines().forEach(System.out::println);
            bf.close();
            out.close();
            clientSocket.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
