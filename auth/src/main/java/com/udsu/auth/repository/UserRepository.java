package com.udsu.auth.repository;

import com.udsu.auth.model.auth.User;
import com.udsu.auth.repository.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final DatabaseClient userDatabase;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Mono<User> findByLogin(String login) {
        final String sql = """
                        SELECT u.id as user_id, u.email as user_email, u.password as user_password, r.id as role_id, r.name as role_name FROM users u
                        LEFT JOIN user_and_role uar ON u.id = uar.user_id
                        LEFT JOIN role r ON uar.role_id = r.id WHERE LOWER(email) = :email
                """;
        return userDatabase.sql(sql)
                .bind("email", login.toLowerCase())
                .map(UserMapper::map)
                .all()
                .collectList()
                .map(UserMapper::mapWithManyRole);
    }
}
