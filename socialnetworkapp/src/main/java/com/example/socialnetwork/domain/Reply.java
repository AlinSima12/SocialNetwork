package com.example.socialnetwork.domain;

public class Reply extends Message{
    private Long replyMessageId;
    public Reply(Long from, Long to, String message, Long replyMessageId) {
        super(from, to, message);
        this.replyMessageId = replyMessageId;
    }

    public Long getReplyMessageId() {
        return replyMessageId;
    }

    public void setReplyMessageId(Long replyMessageId) {
        this.replyMessageId = replyMessageId;
    }

//    @Override
//    public String toString() {
//        return  getMessage() + "        //REPLY//";
//    }
}
