package com.example.socialnetwork.domain;

import java.time.LocalDateTime;

public class Message extends Entity<Long> {
    private Long from;
    private Long to;
    private String message;
    private LocalDateTime date;

    public Message(Long from, Long to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return  message;
    }
}
