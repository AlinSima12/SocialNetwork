package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.Tuple;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.repository.Repository;
import com.example.socialnetwork.server.Client;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class ServiceFriendship {
    private final Client client;
    public ServiceFriendship(Client client) {
        this.client = client;
    }

    public ArrayList<User> getFriends(Long id){
        ArrayList<User> rez = new ArrayList<>();
        //repoFriendship.findAll().forEach(x->{
        client.requestFriendships().forEach(x->{
            if(x.getId().getLeft().equals(id)){
                rez.add(client.requestUser(x.getId().getRight()));
            }
            if(x.getId().getRight().equals(id)){
                rez.add(client.requestUser(x.getId().getLeft()));
            }
        });
        return rez;
    }

    public ArrayList<Pair<User, LocalDate>> getFriendsWithLocalDate(Long id, int month){
        ArrayList<Pair<User, LocalDate>> rez = new ArrayList<>();
        //repoFriendship.findAll().forEach(x->{
        client.requestFriendships().forEach(x->{
            if(x.getDate().getMonth().equals(Month.of(month))) {
                if (x.getId().getLeft().equals(id)) {
                    //rez.add(new Pair<>(repoUser.findOne(x.getId().getRight()).get(), x.getDate()));
                    rez.add(new Pair<>(client.requestUser(x.getId().getRight()), x.getDate()));
                }
                if (x.getId().getRight().equals(id)) {
                    //rez.add(new Pair<>(repoUser.findOne(x.getId().getLeft()).get(), x.getDate()));
                    rez.add(new Pair<>(client.requestUser(x.getId().getLeft()), x.getDate()));
                }
            }
        });
        return rez;
    }

    public Friendship add(Long id1, Long id2) {
        //if (repoUser.findOne(id1).isEmpty()){
        if (client.requestUser(id1) == null){
            throw new IllegalArgumentException("The first ID doesn't exist");
        }
        //if (repoUser.findOne(id2).isEmpty()) {
        if (client.requestUser(id2) == null) {
            throw new IllegalArgumentException("The second ID doesn't exist");
        }
        for (Friendship f : client.requestFriendships()){
            if (f.getId().getRight().equals(id1) && f.getId().getLeft().equals(id2)
                    || f.getId().getRight().equals(id2) && f.getId().getLeft().equals(id1)){
                throw new IllegalArgumentException("This friendship already exists");
            }
        }
        Friendship f = new Friendship();
        f.setId(new Tuple<>(id1, id2));
        //Optional<Friendship> rez = repoFriendship.save(f);
        client.addFriendship(f);
        return null;
    }

    public Friendship delete(Long id1, Long id2) {
        Tuple<Long, Long> id = new Tuple<>(id1, id2);
        client.deleteFriendship(id);
        return null;
    }

//    public void setTheNumberForEachCommunity(HashMap<Long, Integer> users_community) {
//        HashMap<User, Boolean> users_map = new HashMap<>();
//        int nr = 0;
//        for (User user : repoUser.findAll()) {
//            users_map.put(user, false);
//        }
//        for (User user : users_map.keySet()) {
//            if (!users_map.get(user)) {
//                nr++;
//                bfs(user, users_map, nr, users_community);
//            }
//        }
//    }
//
//    private void bfs(User source, Map<User, Boolean> users, int nr, HashMap<Long, Integer> users_community) {
//        ArrayDeque<User> q = new ArrayDeque<>();
//        q.add(source);
//        users_community.put(source.getId(), nr);
//        while (!q.isEmpty()) {
//            User user_curent = q.pop();
//            for (User friend : getFriends(user_curent.getId())) {
//                if (!users.get(friend)) {
//                    q.add(friend);
//                    users.replace(friend, true);
//                    users_community.put(friend.getId(), nr);
//                }
//            }
//        }
//    }
//
//    private int dfs(User source){
//        int maxlen = -1;
//        Stack<Pair<User, Integer>> s = new Stack<>();
//        HashMap<User, Boolean> users_map = new HashMap<>();
//        for (User user : repoUser.findAll()) {
//            users_map.put(user, false);
//        }
//        s.add(new Pair<>(source, 0));
//        users_map.replace(source, true);
//        while (!s.isEmpty()) {
//            Pair<User, Integer> user_curent_pair = s.pop();
//            boolean next = false;
//            for (User friend : getFriends(user_curent_pair.getKey().getId())) {
//                if (!users_map.get(friend)) {
//                    next = true;
//                    s.push(new Pair<>(friend, user_curent_pair.getValue() + 1));
//                    users_map.replace(friend, true);
//                }
//                if(!next){
//                    if (user_curent_pair.getValue() > maxlen)
//                        maxlen = user_curent_pair.getValue();
//                }
//            }
//        }
//
//        return maxlen;
//    }
//    public int getTheMostSociableCommunity() {
//        HashMap<Long, Integer> users_community = new HashMap<>();
//        int max = 0, community = 0;
//        setTheNumberForEachCommunity(users_community);
//        System.out.println(users_community);
//        for (User user : repoUser.findAll()){
//            int localmaxlen = dfs(user);
//            if (localmaxlen > max ){
//                max = localmaxlen;
//                community = users_community.get(user.getId());
//            }
//        }
//        return community;
//    }

    public Iterable<Friendship> findAll(){
        return client.requestFriendships();
    }
}
