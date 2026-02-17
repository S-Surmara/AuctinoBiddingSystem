package org.example.Service;

import org.example.Entity.AuthenticationObject;
import org.example.Persistance.UserRepositry;

public class AuthenticateViaRsaToken extends AuthenticationService{

    public  AuthenticateViaRsaToken(UserRepositry userRepositry){
        super(userRepositry);
    }
    @Override
    public boolean login(AuthenticationObject authenticationObject){
        return true; // need to implement
    }
}
