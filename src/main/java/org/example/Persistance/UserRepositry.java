package org.example.Persistance;

import org.example.Entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class UserRepositry {
    private Map<String,User> userMap;
    public UserRepositry(){
        this.userMap = new HashMap<>();
    }

    public User getUserById(String userId){
        return userMap.get(userId);
    }

    public void saveUser(User user){
        userMap.put(user.getUserId(),user);
    }

    public List<User> getAllUsers(){
        return new ArrayList<>(userMap.values());
    }

    public abstract boolean login(String username,String password);
}
