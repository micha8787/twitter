package bgu.spl.net.api.bidi;

import bgu.spl.net.api.Message;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.Messages.*;

//added the fields and wrote the functions

public class BgsMessageProtocol implements BidiMessagingProtocol<Message>
{
    private ConnectionsImpl connections;
    private int connectionId;
    private boolean shouldTerminate = false;
    DataBase dataBase = DataBase.getinstance();

    @Override
    public void start(int connectionId, Connections connections)
    {
        this.connections = (ConnectionsImpl) connections;
        this.connectionId = connectionId;

    }

    @Override
    public void process(Message message)
    {
        //System.out.println(message.getOpCode());
        if(!(message instanceof RegisterRequest)&!(message instanceof LoginMessage))
        {

            if(dataBase.getUserByConnectionId(connectionId) != null && dataBase.getUserByConnectionId(connectionId).isLoggedIn())
            message.process(connectionId, connections);

            else
                {
                    this.connections.send(this.connectionId,new ErrorMessage(message.getOpCode() ));
                }
        }
        else
        {
            message.process(connectionId,connections);
        }





        //Message msg = null;F

        /*if(!connections.send(this.connectionId, msg))
        {
            //to do. no such connection id
            //to ask if necessarry(mike)
        }
        */



        /*if(message instanceof LogoutRequest)
        {
            if (!dataBase.registeredUsers.containsKey(connectionId))
            {

            }
            //to do check if the client registerd first.
            //but we need ti implement the clients class

        }
        else if(message instanceof LoginMessage)
        {

        }*/




    }

    @Override
    public boolean shouldTerminate()
    {
        return shouldTerminate;
    }


}
