package bgu.spl.net.srv.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.DataBase;

public class UserListRequest implements Message {

    private short OpCode=7;
    DataBase dataBase=DataBase.getinstance();
    short numOfNames;
    public UserListRequest()
    {

    }

    @Override
    public void process(int connectionId, Connections connections)
    {
        String names="";
         numOfNames =(short) dataBase.registeredUsers.keySet().size();
        for (String name:dataBase.registeredUsers.keySet())
        {
            names = names + name + '\0';

        }
        connections.send(connectionId,new Ack(numOfNames,OpCode,names));
    }

    public short getNumOfNames() {
        return numOfNames;
    }

    @Override
    public short getOpCode()
    {
        return OpCode;
    }
}
