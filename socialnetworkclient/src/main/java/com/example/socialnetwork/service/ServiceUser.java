package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.server.Client;
import com.example.socialnetwork.validators.ValidationException;

import java.util.*;

public class ServiceUser {
    private final Client client;

    public ServiceUser(Client client) {
        this.client = client;
    }

    public User add(String firstname, String lastname, String password) {
        validateName(firstname, lastname);
        User usr = new User(firstname, lastname, password);
        //Optional <User> rez = userRepository.save(usr);
        client.addUser(usr);
        return null;
    }

    public User delete(Long id) {
        //Optional <User> rez = userRepository.delete(id);
        client.deleteUser(id);
        return null;
    }
    public ArrayList<User> getFriends(Long id){
        ArrayList<User> rez = new ArrayList<>();
        //friendshipRepository.findAll().forEach(x->{
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

    public User update(String firstname, String lastname, String password, Long id) {
        validateName(firstname, lastname);
        User usr = new User(firstname, lastname, password);
        usr.setId(id);
        //Optional <User> rez = userRepository.update(usr);
        client.updateUser(usr);
        return null;
    }

    static void validateName(String firstname, String lastname){
        if(firstname.matches(".*[0-9].*") || !(Character.isUpperCase(firstname.charAt(0)))) {
            throw new ValidationException("FirstName must not contain numbers and must start with uppercase character!");
        }
        if(lastname.matches(".*[0-9].*") || !(Character.isUpperCase(lastname.charAt(0)))) {
            throw new ValidationException("LastName must not contain numbers and must start with uppercase character!");
        }
    }
//
//    public int getNumberOfCommunities() {
//        HashMap<User, Boolean> users_map = new HashMap<>();
//        int nr = 0;
//        for (User user : findAll()) {
//            users_map.put(user, false);
//        }
//        for (User user : users_map.keySet()) {
//            if (!users_map.get(user)) {
//                nr++;
//                bfs(user, users_map);
//            }
//        }
//        return nr;
//    }
//
//    private void bfs(User source, Map<User, Boolean> users) {
//        ArrayDeque<User> q = new ArrayDeque<>();
//        q.add(source);
//        while (!q.isEmpty()) {
//            User user_current = q.pop();
//            for (User friend : getFriends(user_current.getId())) {
//                if (!users.get(friend)) {
//                    q.add(friend);
//                    users.replace(friend, true);
//                }
//            }
//        }
//    }

    public Iterable<User> findAll() {
        //return userRepository.findAll();
        return client.requestUsers();
    }

    public Iterable<User> findAllPage(Integer pageIndex) {
        return client.requestUsersPage(pageIndex);
    }

    public User findOne(Long id){
        //return userRepository.findOne(id).get();
        return client.requestUser(id);
    }
    public Long getIdUser(String firstname, String lastname, String password){
        return client.getIdUser(firstname, lastname, password);
    }
}
