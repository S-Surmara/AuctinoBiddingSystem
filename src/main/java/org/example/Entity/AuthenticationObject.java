package org.example.Entity;

public class AuthenticationObject {
    String username;
    String password;
    String token;
    String passkey;

    private static class Builder {
        AuthenticationObject authenticationObject;
        public Builder(){
            authenticationObject = new AuthenticationObject();
        }
        public Builder withUsername(String username){
            authenticationObject.username = username;
            return this;
        }
        public Builder withPassword(String password){
            authenticationObject.password = password;
            return this;
        }

        public Builder withToken(String token){
            authenticationObject.token = token;
            return this;
        }

        public Builder withPasskey(String passkey){
            authenticationObject.passkey = passkey;
            return this;
        }

        public AuthenticationObject build(){
            return authenticationObject;
        }
    }
}
