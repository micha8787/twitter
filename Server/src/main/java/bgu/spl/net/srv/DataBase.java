package bgu.spl.net.srv;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DataBase {
//to change to concurent?
    public Map <String,User> registeredUsers=new HashMap<String, User>();//hashmap of registered users with a username  key
   // public Map <User,ArrayList<User>> followersOfUser = new HashMap<User,ArrayList<User>>(); //hashmap of followers of user for each user
  //  public Map <User,ArrayList<String>> followUsers = new HashMap<User,ArrayList<String>>();//hashmap of users who follow a specific user

    private static class SingeltonHolder
    {
        private static DataBase INSTANCE = new DataBase();
    }

    public User getUserByConnectionId(int connectionid)
    {
        for (User user:registeredUsers.values())
        {
            if (user.getConnectionid() == connectionid)
            {
                return user;
            }
        }
        return null;
    }

//    public boolean isfollowing(User user,String stocker)
//    {
//        ArrayList<String>  users=followUsers.get(user);
//        for (String s:users)
//        {
//            if(stocker.equals(s))
//            {
//                return true;
//            }
//        }
//        return false;
//    }



    public static DataBase getinstance()
    {
        return SingeltonHolder.INSTANCE;
    }
}
