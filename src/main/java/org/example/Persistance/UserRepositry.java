package org.example.Persistance;

import org.example.Entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class UserRepositry {
    private final Map<String, User> userMap;

    public UserRepositry(){
        this.userMap = new ConcurrentHashMap<>();
    }

    public synchronized User getUserById(String userId){
        return userMap.get(userId);
    }

    public synchronized void saveUser(User user){
        userMap.put(user.getUserId(), user);
    }

    public synchronized List<User> getAllUsers(){
        return new ArrayList<>(userMap.values());
    }

    public abstract boolean login(String username, String password);
}
