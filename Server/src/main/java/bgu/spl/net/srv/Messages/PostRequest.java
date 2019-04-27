package bgu.spl.net.srv.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.DataBase;

import java.util.ArrayList;

public class PostRequest implements Message {
    private short OpCode=5;
    private String content;
    DataBase dataBase=DataBase.getinstance();
    public PostRequest(String content) {
        this.content = content;
    }

    @Override
    public void process(int connectionId, Connections connections)
    {
        ArrayList<String> attachedNames = new ArrayList<>();
        String senderName = dataBase.getUserByConnectionId(connectionId).getUserName();
        Message notif = new Notification(false, senderName, content);



        dataBase.getUserByConnectionId(connectionId).getPostedMessages().add(notif);


        for(int i= 0; i < content.length(); i = i + 1)
        {

            if(content.charAt(i) == '@')
            {
                //String tmp = content.substring(i + 1);
                int j = i + 1;

                while(content.charAt(j) != ' ')
                {
                    j = j + 1;

                }
                String name = content.substring(i +1, j);
                if(!dataBase.getUserByConnectionId(connectionId).isFollowedBy(name))
                attachedNames.add(name);


            }
        }

        for(String name: dataBase.getUserByConnectionId(connectionId).getFollowers())
        {
            if(dataBase.registeredUsers.get(name).isLoggedIn())
            {
                connections.send(dataBase.registeredUsers.get(name).getConnectionid(), notif);

            }
            else
                {
                    dataBase.registeredUsers.get(name).getmessagetosend().add(notif);
                }




        }

        for(String name : attachedNames)
        {
            if(dataBase.registeredUsers.get(name).isLoggedIn())
            {
                connections.send(dataBase.registeredUsers.get(name).getConnectionid(), notif);

            }
            else
                {
                    dataBase.registeredUsers.get(name).getmessagetosend().add(notif);
                }

        }

        connections.send(connectionId,new Ack((short) 5));



    }

    public short getOpCode() {
        return OpCode;
    }

    public String getContent() {
        return content;
    }
}
