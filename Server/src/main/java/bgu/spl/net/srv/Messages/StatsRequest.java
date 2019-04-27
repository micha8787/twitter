package bgu.spl.net.srv.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.User;

public class StatsRequest implements Message {
    private short OpCode=8;
    private String UserName;
    DataBase dataBase=DataBase.getinstance();
    public StatsRequest(String userName) {
        UserName = userName;
    }

    @Override
    public short getOpCode() {
        return OpCode;
    }

    public String getUserName() {
        return UserName;
    }


    @Override
    public void process(int connectionId, Connections connections)
    {
       User user =  dataBase.getUserByConnectionId(connectionId);
       short numOfPosts =(short) user.getPostedMessages().size();
       short numOfFollowers = (short) user.getFollowing().size();
       short numOfFollowing = (short) user.getFollowers().size();

       connections.send(connectionId, new Ack( numOfPosts, numOfFollowers, numOfFollowing ));
    }
}
