package db.manager;

import db.memory.UserDatabase;
import model.User;

import java.util.Collection;

public class UserDatabaseManager {
    public static void addUser_DB(User user) {
        db.H2.UserDatabase.addUser(user);
    }

    public static User findUserById_DB(String userId) {
        return db.H2.UserDatabase.findUserById(userId);
    }

    public static Collection<User> findAll_DB() {
        return db.H2.UserDatabase.findAll();
    }

    public static void addUser_MEM(User user) {
        db.memory.UserDatabase.addUser(user);
    }

    public static User findUserById_MEM(String userId) {
        return db.memory.UserDatabase.findUserById(userId);
    }

    public static Collection<User> findAll_MEM() {
        return db.memory.UserDatabase.findAll();
    }

}
