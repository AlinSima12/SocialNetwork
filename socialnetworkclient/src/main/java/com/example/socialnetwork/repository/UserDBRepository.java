package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserDBRepository implements Repository<Long, User> {
    protected String url;
    protected String username;
    protected String password;
    private Validator<Long, User> validator;
    public UserDBRepository(String url, String username, String password, Validator<Long, User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Optional<User> findOne(Long longID) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from users " +
                    "where id = ?");
        ) {
            statement.setInt(1, Math.toIntExact(longID));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password = resultSet.getString("password");
                password = CodulLuiCezar.decriptareCezar(password, 1);
                User u = new User(firstName,lastName, password);
                u.setId(longID);
                return Optional.ofNullable(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next())
            {
                Long id= resultSet.getLong("id");
                String firstName=resultSet.getString("first_name");
                String lastName=resultSet.getString("last_name");
                String password = resultSet.getString("password");
                password = CodulLuiCezar.decriptareCezar(password, 1);
                User user=new User(firstName,lastName, password);
                user.setId(id);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<User> save(User entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into users(first_name, last_name, password) VALUES (?,?, ?)");
        ) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, CodulLuiCezar.criptareCezar(entity.getPassword(), 1));
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<User> delete(Long aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("""
                     delete from socialnetwork.public.friendships
                     where id1 = ? OR id2 = ?;
                     delete from socialnetwork.public.users
                     where id = ?
                     """);
        ) {
            statement.setInt(1, Math.toIntExact(aLong));
            statement.setInt(2, Math.toIntExact(aLong));
            statement.setInt(3, Math.toIntExact(aLong));
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<User> update(User entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("""
                     update socialnetwork.public.users
                     set first_name = ?, last_name = ?, password = ?
                     where id = ?;
                     """);
        ) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, CodulLuiCezar.criptareCezar(entity.getPassword(), 1));
            statement.setInt(4, Math.toIntExact(entity.getId()));
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
