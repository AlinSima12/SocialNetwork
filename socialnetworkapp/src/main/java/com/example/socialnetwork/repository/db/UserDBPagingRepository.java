package com.example.socialnetwork.repository.db;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.repository.CodulLuiCezar;
import com.example.socialnetwork.repository.UserDBRepository;
import com.example.socialnetwork.repository.paging.Page;
import com.example.socialnetwork.repository.paging.PageImplementation;
import com.example.socialnetwork.repository.paging.Pageable;
import com.example.socialnetwork.repository.paging.PagingRepository;
import com.example.socialnetwork.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDBPagingRepository extends UserDBRepository implements PagingRepository<Long, User>
{


    public UserDBPagingRepository(String url, String username, String password, Validator validator) {
        super(url, username, password, validator);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
//        Stream<User> result = StreamSupport.stream(this.findAll().spliterator(), false)
//                .skip(pageable.getPageNumber()  * pageable.getPageSize())
//                .limit(pageable.getPageSize());
//        return new PageImplementation<>(pageable, result);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users limit ? offset ?");
        ) {
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
            ResultSet resultSet = statement.executeQuery();
            Set<User> users = new HashSet<>();

            while (resultSet.next())
            {
                Long id= resultSet.getLong("id");
                String firstName=resultSet.getString("first_name");
                String lastName=resultSet.getString("last_name");
                String password=resultSet.getString("password");
                password = CodulLuiCezar.decriptareCezar(password, 1);
                User user=new User(firstName,lastName, password);
                user.setId(id);
                users.add(user);

            }
            return new PageImplementation<User>(pageable, users.stream());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}


