package com.zrb41.utils;

import com.zrb41.entity.User;

public class UserHolder {
    private static ThreadLocal<User> t1=new ThreadLocal<>();

    public static void setUser(User user){
        t1.set(user);
    }
    public static User getUser(){
        return t1.get();
    }
    public static void removeUser(){
        t1.remove();
    }

}
