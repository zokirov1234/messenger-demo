package uz.pdp.messenger.util;

import org.springframework.stereotype.Service;
import uz.pdp.messenger.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public interface UserImpl  extends Base{
    boolean add(User user);

    default Connection getConnection(){
        try{
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/TgBoot",
                    "postgres",
                    "root123"
            );
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
