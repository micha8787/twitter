package bgu.spl.net.srv.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.User;

public class LogoutRequest implements Message {
    private short OpCode=3;
    private String Username;
    private String password;
    DataBase dataBase=DataBase.getinstance();
    public LogoutRequest(String username, String password)
    {
        Username = username;
        this.password = password;
    }

    public LogoutRequest()
    {

    }


    @Override
    public void process(int connectionId, Connections connections)
    {
        User user = dataBase.getUserByConnectionId(connectionId);
        if (user != null && user.isLoggedIn())
        {
            dataBase.getUserByConnectionId(connectionId).setIsloggedin(false);
            connections.send(connectionId,new Ack((short)3));
            connections.disconnect(connectionId);

        }
        else
        {
            connections.send(connectionId,new ErrorMessage((short)3));
        }
    }

    @Override
    public short getOpCode()
    {
        return OpCode;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return password;
    }
}
