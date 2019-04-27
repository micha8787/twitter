package bgu.spl.net.srv.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.NonBlockingConnectionHandler;
import bgu.spl.net.srv.User;

public class RegisterRequest implements Message {

        private short OpCode=1;
        private String Username;
        private String password;
        DataBase dataBase=DataBase.getinstance();
        public RegisterRequest(String username, String password)
        {

            Username = username;
            this.password = password;

        }

    @Override
    public void process(int connectionId, Connections connections)
    {
        System.out.println("processing");

        if (!dataBase.registeredUsers.containsKey(Username))// the client isnt registered yet
        {

            User user = new User(Username,password,connectionId);


            //nullpointer exeption above also
            //((NonBlockingConnectionHandler)((ConnectionsImpl) connections).getHandlersList().get(connectionId)).user = user;
            dataBase.registeredUsers.put(Username,user);
            Ack ack = new Ack( (short)1);
            //System.out.println("connections==null: "+(connections==null));
            connections.send(connectionId,ack);
        }
        else //if the client is already registered-print error
        {
            connections.send(connectionId,new ErrorMessage((short)1));
        }
    }

    public RegisterRequest(String username)
        {
            this.Username = username;
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
