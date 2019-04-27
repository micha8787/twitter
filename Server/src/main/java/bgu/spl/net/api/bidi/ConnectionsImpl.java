package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.DataBase;

import java.io.IOException;
import java.util.HashMap;



//Made by simon


public class ConnectionsImpl<T> implements  Connections<T>
{

    private HashMap<Integer,ConnectionHandler> handlersList;
    private DataBase dataBase = DataBase.getinstance();

    public ConnectionsImpl()
    {
        this.handlersList = new HashMap<>();
    }

    @Override
    public boolean send(int connectionId, T msg)
    {
        //Check if there's a con handler with given ID - sends the msg if it does(returns true) else returns false
        boolean isSent;
       // System.out.println(connectionId + " con id");
       // System.out.println(handlersList.keySet());
       // System.out.println(handlersList.containsKey(0));

        if(this.handlersList.containsKey(connectionId))
        {
           // System.out.println("SENT");
            isSent = true;
            this.handlersList.get(connectionId).send(msg);


        }
        else
        {
           // System.out.println("not sent");
            isSent = false;
        }

        return isSent;


    }

    @Override
    public void broadcast(T msg)
    {
        for (HashMap.Entry<Integer, ConnectionHandler> entry : this.handlersList.entrySet())
        {
            this.handlersList.get(entry.getKey()).send(msg);
        }

    }

    @Override
    public void disconnect(int connectionId)
    {
        try {
            this.handlersList.get(connectionId).close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.handlersList.remove(connectionId);
    }

    public HashMap<Integer,ConnectionHandler> getHandlersList()
    {
        return this.handlersList;
    }
}

