package com.VrikkaDuck.dpgdrawsdiscord;

import com.VrikkaDuck.dpgdrawsdiscord.packet.Packet;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DiscordHandler extends Thread {

    public DiscordHandler(){

    }


    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(5000);

            while (true) {
                // System.out.println("Waiting for packet...");
                Socket clientSocket = serverSocket.accept();
                // System.out.println("Packet received");

                // Create a new thread to handle the client connection
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {

                if (Variables.configHandler == null){
                    MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();

                    if (server == null){
                        throw new NullPointerException();
                    }

                    Variables.configHandler = new ConfigHandler(server);
                }

                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String packetJson;
                while ((packetJson = in.readLine()) != null) {

                    // System.out.println(packetJson);

                    // Deserialize the packet from JSON
                    Packet packet = Packet.FromJson(packetJson);

                    assert packet != null;
                    String response = packet.ProcessResponse();
                    out.println(response);
                }

                // Close the connections
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
