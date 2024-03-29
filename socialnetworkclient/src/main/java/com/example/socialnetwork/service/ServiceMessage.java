package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.*;
import com.example.socialnetwork.server.Client;

import java.util.ArrayList;

public class ServiceMessage {
    private Client client;

    public ServiceMessage(Client client) {
        this.client = client;
    }

    public Message add(Long from, Long to, String message) {
        if (client.requestUser(from) == null){
            throw new IllegalArgumentException("The first ID doesn't exist");
        }
        if (client.requestUser(to) == null) {
            throw new IllegalArgumentException("The second ID doesn't exist");
        }
        Message m = new Message(from, to, message);
        client.addMessage(m);
        return null;
    }

    public void replyMessage(Long idMessage, String message){
        if (client.requestMessage(idMessage) == null){
            throw new IllegalArgumentException("This message doesn't exist");
        }
        //Message m = repoMessage.findOne(idMessage).get();
        Message m = client.requestMessage(idMessage);
        Reply r = new Reply(m.getTo(), m.getFrom(), message, idMessage);
        //repoMessage.replyMessage(r);
        client.replyMessage(r);
    }

    public Message findOne(Long id){
        return client.requestMessage(id);
    }

    public ArrayList <Message> getUserMessages(Long id1, Long id2){
        Friendship f = new Friendship();
        f.setId(new Tuple<>(id1, id2));
        return client.requestFriendshipMessages(f);
        //return repoMessage.getUserMessages(id1, id2);
    }

    public Iterable<Message> findAll(){
        //return repoMessage.findAll();
        return client.requestMessages();
    }
}
