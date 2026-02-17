package org.example;

import org.example.Persistance.UserRepositry;

class SimpleUserRepository extends UserRepositry {
    @Override
    public boolean login(String username, String password) {
        return true;
    }
}
