package bgu.spl.net.srv.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.User;

public class LoginMessage implements Message {

    private short OpCode = 2;
    private String Username;
    private String password;
    DataBase dataBase = DataBase.getinstance();

    public LoginMessage() { }

    @Override
    public void process(int connectionId, Connections connections)
    {
        User user = dataBase.registeredUsers.get(Username);
        if(user != null && user.getPassword().equals(password) && user.isLoggedIn() == false)//the user is exist and the right password and the user isnt logged in yet
        {
            dataBase.registeredUsers.get(Username).setIsloggedin(true);
            dataBase.registeredUsers.get(Username).setConnectionid(connectionId);
            Ack ack = new Ack((short)2);
            connections.send(connectionId,ack);

            if(!dataBase.getUserByConnectionId(connectionId).getmessagetosend().isEmpty())
            {
                for(Message msg : dataBase.getUserByConnectionId(connectionId).getmessagetosend() )
                {
                    connections.send(connectionId, msg);
                }

            }

        }
        else
        {
            ErrorMessage errorMes = new ErrorMessage((short)2);
            connections.send(connectionId,errorMes);
        }
    }

    @Override

    public short getOpCode()
    {
        return OpCode;
    }

    public String getUsername()
    {
        return Username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setUsername(String username)
    {
        Username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
