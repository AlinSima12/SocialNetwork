package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.Request;
import com.example.socialnetwork.domain.Tuple;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.server.Client;

import java.util.ArrayList;

public class ServiceRequests {
    private final Client client;

    public ServiceRequests(Client client) {
        this.client = client;
    }


    public Request add(Long id1, Long id2) {
        //if (repoUser.findOne(id1).isEmpty()){
        if (client.requestUser(id1) == null){
            throw new IllegalArgumentException("The first ID doesn't exist");
        }
        //if (repoUser.findOne(id2).isEmpty()) {
        if (client.requestUser(id2) == null) {
            throw new IllegalArgumentException("The second ID doesn't exist");
        }
        //for (Request f : repoRequests.findAll()){
        for (Request f : client.requestRequests()){
            if (f.getId().getRight().equals(id1) && f.getId().getLeft().equals(id2)
                    || f.getId().getRight().equals(id2) && f.getId().getLeft().equals(id1)){
                throw new IllegalArgumentException("This request already exists");
            }
        }
        Request r = new Request("pending");
        r.setId(new Tuple<>(id1, id2));
        //Optional<Request> rez = repoRequests.save(r);
        client.sendRequest(r);
        return null;
    }

    public void acceptRequest(Long id1, Long id2){
        //if (repoRequests.findOne(new Tuple<>(id1, id2)).isPresent())
        if (client.requestRequest(new Tuple<>(id1, id2)) != null)
            //if(!repoRequests.findOne(new Tuple<>(id1, id2)).get().getStatus().equals("declined")) {
            if(!client.requestRequest(new Tuple<>(id1, id2)).getStatus().equals("declined")) {
                //repoRequests.acceptRequest(id1, id2);
                client.acceptRequest(new Tuple<>(id1, id2));
                Friendship f = new Friendship();
                f.setId(new Tuple<>(id1, id2));
                //repoFriendship.save(f);
                client.addFriendship(f);
            }
    }

    public void declineRequest(Long id1, Long id2){
        //if (repoRequests.findOne(new Tuple<>(id1, id2)).isPresent())
        if (client.requestRequest(new Tuple<>(id1, id2)) != null)
            //if(!repoRequests.findOne(new Tuple<>(id1, id2)).get().getStatus().equals("accepted")) {
            if(!client.requestRequest(new Tuple<>(id1, id2)).getStatus().equals("accepted")) {
                //repoRequests.declineRequest(id1, id2);
                client.declineRequest(new Tuple<>(id1, id2));
            }
    }

    public Request delete(Long id1, Long id2) {
        Tuple<Long, Long> id = new Tuple<>(id1, id2);
        //Optional<Request> rez = repoRequests.delete(id);
        client.deleteRequest(id);
        return null;
    }

    public Iterable<Request> findAll(){
        //return repoRequests.findAll();
        return client.requestRequests();
    }

    public ArrayList<Pair<User, String>> getSentRequests(Long id){
        ArrayList<Pair<User, String>> rez = new ArrayList<>();
        //repoRequests.findAll().forEach(x->{
        client.requestRequests().forEach(x->{
            if(x.getId().getLeft().equals(id)){
                //rez.add(new Pair<>(repoUser.findOne(x.getId().getRight()).get(), x.getStatus()));
                rez.add(new Pair<>(client.requestUser(x.getId().getRight()), x.getStatus()));
            }
        });
        return rez;
    }

    public ArrayList<Pair<User, String>> getReceivedRequests(Long id){
        ArrayList<Pair<User, String>> rez = new ArrayList<>();
        //repoRequests.findAll().forEach(x->{
        client.requestRequests().forEach(x->{
            if(x.getId().getRight().equals(id)){
                //rez.add(new Pair<>(repoUser.findOne(x.getId().getLeft()).get(), x.getStatus()));
                rez.add(new Pair<>(client.requestUser(x.getId().getLeft()), x.getStatus()));
            }
        });
        return rez;
    }
}
