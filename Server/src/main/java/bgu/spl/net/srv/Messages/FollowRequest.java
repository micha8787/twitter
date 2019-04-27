package bgu.spl.net.srv.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.User;

import java.util.ArrayList;

public class FollowRequest implements Message {
    private short OpCode=4;
    DataBase dataBase=DataBase.getinstance();
    private boolean succeded=false;
    public  boolean followOrUnFollow;
  private ArrayList<String> UserNameList = new ArrayList<>();
  private int numOfUsers;
    private ArrayList<String> succededlist = new ArrayList<>();
    private short numOfSucceded;

    public FollowRequest(boolean followOrUnFollow, int numOfUsers)
    {
        this.followOrUnFollow = followOrUnFollow;
        this.numOfUsers = numOfUsers;

    }

    public FollowRequest(boolean followOrUnFollow, ArrayList<String> UserNameList, int numOfUsers)
    {
        this.followOrUnFollow = followOrUnFollow;
        this.UserNameList = UserNameList;
        this.numOfUsers = numOfUsers;
    }

    @Override
    public void process(int connectionId, Connections connections)
    {
        User user = dataBase.getUserByConnectionId(connectionId);

        if (user.isLoggedIn())
        {
            if (this.followOrUnFollow)//if its a follow request
            {
                for (String s : UserNameList)
                {
                    if (!user.isfollowing(s))
                    {
                        user.addUserToFollow(s);
                        succeded = true;

                        User followedUser = dataBase.registeredUsers.get(s);
                //        System.out.println(this.UserNameList.size() + " KAKAKAKAKAKAKAKAKAKAKAKAKA");
                 //       System.out.println(  " FOLLOWED USER IS NULL: " + (followedUser==null));
                  //      System.out.println("  addfollower ");
                        followedUser.addfollower(user.getUserName());
                        succededlist.add(s);
                    }

                }
            }
            else //if its an unfollow request
            {
                for (String s : UserNameList)
                {
                    if (user.isfollowing(s))
                    {
                        user.unfollow(s);
                        succeded = true;
                        User unfollowedUser = dataBase.registeredUsers.get(s);
                        unfollowedUser.removefollower(user.getUserName());
                        succededlist.add(s);
                    }

                }
            }
        }

        if (succeded)
        {
            numOfSucceded = (short)succededlist.size();
            String optional = new String();
            for (String string:succededlist )
            {
                optional = optional+string+'\0';
            }
            Ack ack = new Ack(numOfSucceded,(short)4,optional);
            connections.send(connectionId,ack);

        }
        else
        {
            connections.send(connectionId,new ErrorMessage((short)4));
        }
    }


//     if (FollowOrUnFollow)//if its a follow request
//    {
//        ArrayList<String>  followUsers=dataBase.followUsers.get(user);
//        for (String s:UserNameList)
//        {
//            if (dataBase.isfollowing(user,s))
//            {
//                succeded=true;
//                dataBase.followUsers.
//            }
//        }
//    }

    public boolean isFollowOrUnFollow() {
        return followOrUnFollow;
    }

    public ArrayList<String> getUserNameList() {
        return UserNameList;
    }


    public void AddToUserNameList(String Name) {
        UserNameList.add(Name);

    }


    public int getNumOfUsers() {
        return numOfUsers;
    }

    public short getNumOfSucceded() {
        return numOfSucceded;
    }

    public short getOpCode() {
        return OpCode;
    }
}
