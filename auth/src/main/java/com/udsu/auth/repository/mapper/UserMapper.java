package com.udsu.auth.repository.mapper;

import com.udsu.auth.model.auth.Role;
import com.udsu.auth.model.auth.User;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.List;

public class UserMapper extends BaseMapper{
    public static User map(Row row, RowMetadata rowMetadata) {
        final Long id = getLongVal("id", row, rowMetadata);
        final String email = getStringVal("email", row, rowMetadata);
        final String password = getStringVal("password", row, rowMetadata);
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }

    public static User mapWithRole(Row row, RowMetadata rowMetadata) {
        final Long id = getLongVal("user_id", row, rowMetadata);
        final String email = getStringVal("user_email", row, rowMetadata);
        final String password = getStringVal("user_password", row, rowMetadata);

        final Long roleId = getLongVal("role_id", row, rowMetadata);
        final String roleName = getStringVal("role_name", row, rowMetadata);

        return User.builder()
                .email(email)
                .password(password)
                .roles(new Role[]{new Role(roleId.toString(), roleName)})
                .build();
    }

    public static User mapWithManyRole(List<User> users) {
        final Role[] roles = new Role[users.size()];
        for (int i = 0; i < users.size(); i++) {
            roles[i] = users.get(i).getRoles()[0];
        }
        return User.builder()
                .email(users.get(0).getEmail())
                .password(users.get(0).getPassword())
                .roles(roles)
                .build();
    }
}
