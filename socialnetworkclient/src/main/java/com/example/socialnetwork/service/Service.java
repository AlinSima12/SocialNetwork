package com.example.socialnetwork.service;

public interface Service <ID, E>{
    E add(String first_name, String last_name, ID id);

    E delete(ID id);

}
