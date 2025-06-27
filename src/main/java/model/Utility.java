package model;

import org.mindrot.jbcrypt.BCrypt;

public class Utility {
    public static String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword){
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
