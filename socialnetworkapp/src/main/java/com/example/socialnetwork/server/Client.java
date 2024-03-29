package com.example.socialnetwork.server;

import com.example.socialnetwork.domain.*;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Client{
    private Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean done;

    public Client(Socket client, ObjectInputStream in, ObjectOutputStream out) {
        this.client = client;
        this.in = in;
        this.out = out;
    }

    public ArrayList<Message> requestFriendshipMessages(Friendship friendship){
        Object receivedObject;
        try {
            Command command = Command.REQUEST_MESSAGES_FOR_FRIENDSHIP;
            out.writeObject(new CommunicationObject(command, friendship));
            receivedObject = in.readObject();
            if (receivedObject instanceof CommunicationObject){
                if (((CommunicationObject) receivedObject).getObject() instanceof ArrayList<?>){
                    //System.out.println(((CommunicationObject) receivedObject).getObject());
                }
                else{
                    System.out.println("Continut gresit");
                }
            }
            else{
                System.out.println("Mesaj eronat");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (ArrayList<Message>) ((CommunicationObject) receivedObject).getObject();
    }



    public void deleteFriendship(Tuple<Long, Long> object) {
        Object receivedObject;
        try {
            Command command = Command.DELETE_FRIENDSHIP;
            out.writeObject(new CommunicationObject(command, object));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addFriendship(Friendship object) {
        try {
            Command command = Command.ADD_FRIENDSHIP;
            out.writeObject(new CommunicationObject(command, object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Iterable<Friendship> requestFriendships() {
        Object receivedObject;
        try {
            Command command = Command.REQUEST_FRIENDSHIPS;
            out.writeObject(new CommunicationObject(command, null));
            receivedObject = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (Iterable<Friendship>) ((CommunicationObject) receivedObject).getObject();
    }

    public Friendship requestFriendship(Tuple<Long, Long> object) {
        Object receivedObject;
        try {
            Command command = Command.REQUEST_FRIENDSHIP;
            out.writeObject(new CommunicationObject(command, object));
            receivedObject = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (Friendship) ((CommunicationObject) receivedObject).getObject();
    }

    public void updateUser(User object) {
        try {
            Command command = Command.UPDATE_USER;
            out.writeObject(new CommunicationObject(command, object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(Long object) {
        try {
            Command command = Command.DELETE_USER;
            out.writeObject(new CommunicationObject(command, object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUser(User object) {
        try {
            Command command = Command.ADD_USER;
            out.writeObject(new CommunicationObject(command, object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Iterable<User> requestUsersPage(Integer pageIndex) {
        Object receivedObject;
        try {
            Command command = Command.REQUEST_USERS_PAGE;
            out.writeObject(new CommunicationObject(command, pageIndex));
            receivedObject = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (List<User>) ((CommunicationObject) receivedObject).getObject();
    }

    public Iterable<User> requestUsers() {
        Object receivedObject;
        try {
            Command command = Command.REQUEST_USERS;
            out.writeObject(new CommunicationObject(command, null));
            receivedObject = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (Iterable<User>) ((CommunicationObject) receivedObject).getObject();
    }

    public User requestUser(Long object) {
        Object receivedObject;
        try {
            Command command = Command.REQUEST_USER;
            out.writeObject(new CommunicationObject(command, object));
            receivedObject = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (User) ((CommunicationObject) receivedObject).getObject();
    }

    public void declineRequest(Tuple<Long, Long> object) {
        Object receivedObject;
        try {
            Command command = Command.DECLINE_REQUEST;
            out.writeObject(new CommunicationObject(command, object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void acceptRequest(Tuple<Long, Long> object) {
        try {
            Command command = Command.ACCEPT_REQUEST;
            out.writeObject(new CommunicationObject(command, object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteRequest(Tuple<Long, Long> object) {
        try {
            Command command = Command.DELETE_REQUEST;
            out.writeObject(new CommunicationObject(command, object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRequest(Request object) {
        try {
            Command command = Command.SEND_REQUEST;
            out.writeObject(new CommunicationObject(command, object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Iterable<Request> requestRequests() {
        try {
            Command command = Command.REQUEST_REQUESTS;
            out.writeObject(new CommunicationObject(command, null));
            Object receivedObject = in.readObject();
            return (Iterable<Request>) ((CommunicationObject) receivedObject).getObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Request requestRequest(Tuple<Long, Long> object) {
        Object receivedObject;
        try {
            Command command = Command.REQUEST_REQUEST;
            out.writeObject(new CommunicationObject(command, object));
            receivedObject = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (Request) ((CommunicationObject) receivedObject).getObject();
    }

    public Message requestMessage(Long object) {
        Object receivedObject;
        try {
            Command command = Command.REQUEST_MESSAGE;
            out.writeObject(new CommunicationObject(command, object));
            receivedObject = in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (Message) ((CommunicationObject) receivedObject).getObject();
    }

    public Iterable<Message> requestMessages() {
        Object receivedObject;
        try {
            Command command = Command.REQUEST_MESSAGES;
            out.writeObject(new CommunicationObject(command, null));
            receivedObject = in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (Iterable<Message>) ((CommunicationObject) receivedObject).getObject();
    }

    public void replyMessage(Reply object) {
        try {
            Command command = Command.REPLY_MESSAGE;
            out.writeObject(new CommunicationObject(command, object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<Message> requestMessagesForFriendship (Friendship object){
        Object receivedObject;
        try {
            Command command = Command.REQUEST_MESSAGES_FOR_FRIENDSHIP;
            out.writeObject(new CommunicationObject(command, object));
            receivedObject = in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (ArrayList<Message>) ((CommunicationObject) receivedObject).getObject();
    }

    public void addMessage (Message object){
        try {
            Command command = Command.ADD_MESSAGE;
            out.writeObject(new CommunicationObject(command, object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getIdUser(String firstname, String lastname, String password){
        Object receivedObject;
        try {
            Command command = Command.REQUEST_USER_BY_CREDENTIALS;
            User user = new User(firstname, lastname, password);
            out.writeObject(new CommunicationObject(command, user));
            receivedObject = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (Long) ((CommunicationObject) receivedObject).getObject();
    }





    public void shutdown(){
        done = true;
        try{
            in.close();
            out.close();
            if(!client.isClosed()){
                client.close();
            }
        } catch (IOException e) {
            shutdown();
        }
    }

//    @Override
//    public void run() {
//        InputHandler inHandler = new InputHandler();
//        Thread t = new Thread(inHandler);
//        t.start();
//
//    }

//    class InputHandler implements Runnable{
//        @Override
//        public void run() {
//            try {
//                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
////                while(!done){
////                    String message = inReader.readLine();
////                    Message newMessage = new Message(1L, 2L, message);
////                    CommunicationObject communicationObject = new CommunicationObject(Command.SEND_MESSAGE, newMessage);
////                    out.writeObject(communicationObject);
////                }
//                String messageText = inReader.readLine();
//                Message message = new Message(4L,2L, messageText);
//                sendMessage(message);
//
//                Friendship fr = new Friendship();
//                fr.setId(new Tuple<>(4L, 2L));
//                requestFriendshipMessages(fr);
//            }
//            catch (IOException e){
//                shutdown();
//            }
//        }
//    }

    public static void main(String[] args) throws IOException {
        Socket client = new Socket("127.0.0.1", 7999);
        ObjectInputStream i = new ObjectInputStream(client.getInputStream());
        ObjectOutputStream o = new ObjectOutputStream(client.getOutputStream());
        Client c = new Client(client, i, o);
    }
}
