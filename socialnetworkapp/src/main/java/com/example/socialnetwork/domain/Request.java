package com.example.socialnetwork.domain;

public class Request extends Entity<Tuple<Long, Long>>{
    private String status;


    public Request (String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
