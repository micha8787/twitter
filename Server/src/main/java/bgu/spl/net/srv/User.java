package bgu.spl.net.srv;

import bgu.spl.net.api.Message;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {
   private String userName;
    private String password;
    private boolean isloggedin;
    private int timestamp;
    private int connectionid;
    private ArrayList<String> followers;
    private ArrayList<String> following = new ArrayList<>();
    private ArrayList<Message> messageToSendWhileloggingin = new ArrayList<>();
    private ArrayList<Message> postedMessages = new ArrayList<>();

    public User(String userName, String password,int Connectionid)
    {
        this.userName = userName;
        this.password = password;
        this.connectionid = connectionid;
        isloggedin = false;
        followers = new ArrayList<>();
        following= new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public boolean isLoggedIn()
    {
        return isloggedin;
    }

    public int getConnectionid() {
        return connectionid;
    }

    public ArrayList<Message> getmessagetosend() {
        return messageToSendWhileloggingin;
    }

    public void addMessage(Message message)
    {
        messageToSendWhileloggingin.add(message);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public void setIsloggedin(boolean isloggedin) {
        this.isloggedin = isloggedin;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;

    }
    public boolean isfollowing(String followed)
    {
      return following.contains(followed);
    }
    public boolean isFollowedBy(String following)
    {
        return followers.contains(following);
    }
    public void addfollower(String follower)
    {
        followers.add(follower);
    }
    public void removefollower(String follower)
    {
        followers.remove(follower);
    }
    public void addUserToFollow(String follower)
    {
        following.add(follower);
    }
    public void unfollow(String follower)
    {
        following.remove(follower);
    }

    public void setConnectionid(int connectionid) {
        this.connectionid = connectionid;
    }

    public ArrayList<String> getFollowers()
    {
        return this.followers;
    }

    public ArrayList<String> getFollowing()
    {
        return this.following;
    }

    public ArrayList<Message> getPostedMessages()
    {
        return this.postedMessages;
    }


}
