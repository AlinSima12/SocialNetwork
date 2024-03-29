package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.Request;
import com.example.socialnetwork.domain.Tuple;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
public class RequestsDBRepository implements Repository<Tuple<Long, Long>, Request>{
    private String url;
    private String username;
    private String password;
    public RequestsDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    @Override
    public Optional<Request> findOne(Tuple<Long, Long> longID) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from requests " +
                    "where id1 = ? and id2 = ?");
        ) {
            statement.setInt(1, Math.toIntExact(longID.getLeft()));
            statement.setInt(2, Math.toIntExact(longID.getRight()));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                String status = resultSet.getString("status");
                Request r = new Request(status);
                r.setId(new Tuple<>(id1, id2));
                return Optional.of(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
    @Override
    public Iterable<Request> findAll() {
        Set<Request> requests = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from requests");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next())
            {
                Long id1= resultSet.getLong("id1");
                Long id2= resultSet.getLong("id2");
                String status = resultSet.getString("status");
                Request r = new Request(status);
                r.setId(new Tuple<>(id1, id2));
                requests.add(r);
            }
            return requests;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<Request> save(Request entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into requests(id1, id2, status) VALUES (?, ?, ?)");
        ) {
            statement.setLong(1, Math.toIntExact(entity.getId().getLeft()));
            statement.setLong(2, Math.toIntExact(entity.getId().getRight()));
            statement.setString(3, entity.getStatus());
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<Request> delete(Tuple<Long, Long> aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("""
                     delete from socialnetwork.public.requests
                     where id1 = ? AND id2 = ? OR id1 = ? AND id2 = ?
                     """);
        ) {
            statement.setInt(1, Math.toIntExact(aLong.getLeft()));
            statement.setInt(2, Math.toIntExact(aLong.getRight()));
            statement.setInt(3, Math.toIntExact(aLong.getRight()));
            statement.setInt(4, Math.toIntExact(aLong.getLeft()));
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<Request> update(Request entity) {
        return Optional.empty();
    }

    public void acceptRequest(Long id1, Long id2){
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("""
                     update socialnetwork.public.requests
                     set status = ?
                     where id1 = ? and id2 = ?;
                     """);
        ) {
            statement.setString(1, "accepted");
            statement.setInt(2, Math.toIntExact(id1));
            statement.setInt(3, Math.toIntExact(id2));
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void declineRequest(Long id1, Long id2){
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("""
                     update socialnetwork.public.requests
                     set status = ?
                     where id1 = ? and id2 = ?;
                     """);
        ) {
            statement.setString(1, "declined");
            statement.setInt(2, Math.toIntExact(id1));
            statement.setInt(3, Math.toIntExact(id2));
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
