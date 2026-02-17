package org.example.Service;

import org.example.Entity.AuthenticationObject;
import org.example.Entity.User;
import org.example.Persistance.UserRepositry;

public abstract class AuthenticationService {
    private UserRepositry userRepositry;

    public AuthenticationService(UserRepositry userRepositry){
        this.userRepositry = userRepositry;
    }

    public void signUp(User user){
        userRepositry.saveUser(user);
    }

    public abstract boolean login(AuthenticationObject authenticationObject);
}
