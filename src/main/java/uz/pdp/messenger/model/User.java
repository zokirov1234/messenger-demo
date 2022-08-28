package uz.pdp.messenger.model;

import org.springframework.data.relational.core.sql.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class User extends Base {
    private String name;
    private String username;
    private String password;

    @Override
    protected void get(ResultSet resultSet) {
        try {
            super.getId() = resultSet.getLong("id");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public User(long id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Base64.getEncoder().encodeToString(password.getBytes());
    }

}
