package uz.pdp.messenger.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import uz.pdp.messenger.model.User;
import uz.pdp.messenger.util.UserImpl;

import java.sql.Connection;

@Primary
public class UserService implements UserImpl {


    @Override
    public boolean add(User user) {

    }
}
