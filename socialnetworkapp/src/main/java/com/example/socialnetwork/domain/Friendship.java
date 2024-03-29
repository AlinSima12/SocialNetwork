package com.example.socialnetwork.domain;

import java.time.LocalDate;


public class Friendship extends Entity<Tuple<Long,Long>> {

    LocalDate date;

    public Friendship() {
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString(){
        return "ID1: " + getId().getLeft() + " ID2: " + getId().getRight() + " Date: " + getDate();
    }
}
