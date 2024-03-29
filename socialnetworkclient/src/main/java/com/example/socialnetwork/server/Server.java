package com.example.socialnetwork.server;

import com.example.socialnetwork.domain.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    private ArrayList<ConnectionHandler> connectionHandlers;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;

    public Server(){
        connectionHandlers = new ArrayList<ConnectionHandler>();
        done = false;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(client);
                connectionHandlers.add(connectionHandler);
                pool.execute(connectionHandler);
            }
        } catch (IOException e) {
            shutdown();
            System.out.println("Serverul a fost inchis");
        }
    }

    public static void main(String[] args) {
        System.out.println("Server Social Network");
        Server server = new Server();
        server.run();
    }
    private void shutdown() {
        try {
            done = true;
            if (!server.isClosed()) {
                server.close();
            }
            for (ConnectionHandler ch : connectionHandlers)
                ch.shutdown();
        }
        catch (IOException e){
                //ignore
            }
    }
    class ConnectionHandler implements Runnable{
        private Socket client;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        public ConnectionHandler(Socket client){
            this.client = client;
        }

        @Override
        public void run() {
            try{
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());

                Message messageTest = new Message(1L, 2L, "SERVER: Welcome!");
                out.writeObject(messageTest);

                Object receivedObject;
                while ((receivedObject = in.readObject()) != null){
                    if (receivedObject instanceof Message) {
                        Message message = (Message) receivedObject;
                        System.out.println("Mesajul primit de la client: " + message.getFrom() + ", " + message.getTo() + ", " + message.getMessage());
                    }
                    else{
                        System.out.println("Mesaj necunoscut!!!");
                    }
                }
            }
            catch (IOException e){
                shutdown();
                System.out.println("Serverul a fost inchis");
            }
            catch (ClassNotFoundException e) {
                System.out.println("Clasa necunoscut!!!");
            }
        }
        public void sendMessageObject(Message message) throws IOException {
            out.writeObject(message);
        }
        private void shutdown(){
            try {
                in.close();
                out.close();
                if(!client.isClosed())
                    client.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }
}
