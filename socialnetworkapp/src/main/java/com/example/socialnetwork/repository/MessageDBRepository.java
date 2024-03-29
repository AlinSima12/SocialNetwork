package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.*;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
public class MessageDBRepository implements Repository<Long, Message>{
    private String url;
    private String username;
    private String password;
    public MessageDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    @Override
    public Optional<Message> findOne(Long longID) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from messages where id = ?");
        ) {
            statement.setInt(1, Math.toIntExact(longID));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long from = resultSet.getLong("sentfrom");
                Long to = resultSet.getLong("sentto");
                String message = resultSet.getString("message");
                Date date = resultSet.getDate("date");
                Long replyMessageId = resultSet.getLong("repliedMessageId");
                LocalDateTime dateTime = Instant.ofEpochMilli(date.getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().atStartOfDay();
                if (replyMessageId == 0) {
                    Message m = new Message(from, to, message);
                    m.setId(id);
                    m.setDate(dateTime);
                    return Optional.of(m);
                }
                else {
                    Reply m = new Reply(from, to, message, replyMessageId);
                    m.setId(id);
                    m.setDate(dateTime);
                    return Optional.of(m);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from messages");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next())
            {
                Long id = resultSet.getLong("id");
                Long from = resultSet.getLong("sentfrom");
                Long to = resultSet.getLong("sentto");
                String message = resultSet.getString("message");
                Date date = resultSet.getDate("date");
                Long replyMessageId = resultSet.getLong("repliedMessageId");
                LocalDateTime dateTime = Instant.ofEpochMilli(date.getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().atStartOfDay();
                if (replyMessageId == 0) {
                    Message m = new Message(from, to, message);
                    m.setId(id);
                    m.setDate(dateTime);
                    messages.add(m);
                }
                else {
                    Reply m = new Reply(from, to, message, replyMessageId);
                    m.setId(id);
                    m.setDate(dateTime);
                    messages.add(m);
                }
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Message> getUserMessages(Long id1, Long id2) {
        ArrayList<Message> messages = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from messages where (sentfrom = ? and sentto = ?) or (sentfrom = ? and sentto = ?) order by date");

        ) {
            statement.setLong(1, Math.toIntExact(id1));
            statement.setLong(2, Math.toIntExact(id2));
            statement.setLong(4, Math.toIntExact(id1));
            statement.setLong(3, Math.toIntExact(id2));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                Long id = resultSet.getLong("id");
                Long from = resultSet.getLong("sentfrom");
                Long to = resultSet.getLong("sentto");
                String message = resultSet.getString("message");
                Date date = resultSet.getDate("date");
                Long replyMessageId = resultSet.getLong("repliedmessageid");
                LocalDateTime dateTime = Instant.ofEpochMilli(date.getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().atStartOfDay();
                if (replyMessageId == 0) {
                    Message m = new Message(from, to, message);
                    m.setId(id);
                    m.setDate(dateTime);
                    messages.add(m);
                }
                else {
                    Reply m = new Reply(from, to, message, replyMessageId);
                    m.setId(id);
                    m.setDate(dateTime);
                    messages.add(m);
                }
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<Message> save(Message entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into messages(sentfrom, sentto, message, repliedmessageid) VALUES (?, ?, ?, ?)");
        ) {
            statement.setLong(1, Math.toIntExact(entity.getFrom()));
            statement.setLong(2, Math.toIntExact(entity.getTo()));
            statement.setString(3, entity.getMessage());
            statement.setNull(4, Types.BIGINT);
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<Message> delete(Long aLong) {
        return null;
    }
    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }

    public void replyMessage(Reply r){
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into messages(sentfrom, sentto, message, repliedmessageid) VALUES (?, ?, ?, ?)");
        ) {
            statement.setLong(1, Math.toIntExact(r.getFrom()));
            statement.setLong(2, Math.toIntExact(r.getTo()));
            statement.setString(3, r.getMessage());
            statement.setLong(4, Math.toIntExact(r.getReplyMessageId()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
