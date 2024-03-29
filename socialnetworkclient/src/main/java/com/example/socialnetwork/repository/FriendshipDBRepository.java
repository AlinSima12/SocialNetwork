package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.Tuple;
import com.example.socialnetwork.validators.Validator;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
public class FriendshipDBRepository implements Repository<Tuple<Long, Long>, Friendship>{
    private String url;
    private String username;
    private String password;
    private Validator<Tuple<Long, Long>, Friendship> validator;
    public FriendshipDBRepository(String url, String username, String password, Validator<Tuple<Long, Long>, Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Optional<Friendship> findOne(Tuple<Long, Long> longID) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from friendships " +
                    "where id1 = ? and id2 = ?");
        ) {
            statement.setInt(1, Math.toIntExact(longID.getLeft()));
            statement.setInt(2, Math.toIntExact(longID.getRight()));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                LocalDate date = resultSet.getDate("data").toLocalDate();
                Friendship f = new Friendship();
                f.setId(new Tuple<>(id1, id2));
                f.setDate(date);
                return Optional.of(f);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from friendships");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next())
            {
                Long id1= resultSet.getLong("id1");
                Long id2= resultSet.getLong("id2");
                LocalDate date = resultSet.getDate("data").toLocalDate();
                Friendship friendship=new Friendship();
                friendship.setId(new Tuple<>(id1, id2));
                friendship.setDate(date);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<Friendship> save(Friendship entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into friendships(id1, id2) VALUES (?, ?)");
        ) {
            statement.setLong(1, Math.toIntExact(entity.getId().getLeft()));
            statement.setLong(2, Math.toIntExact(entity.getId().getRight()));
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<Friendship> delete(Tuple<Long, Long> aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("""
                     delete from socialnetwork.public.friendships
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
    public Optional<Friendship> update(Friendship entity) {
        return Optional.empty();
    }
}
