package bgu.spl.net.srv.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.DataBase;

public class Notification implements Message {
    private short OpCode = 9;
    private boolean isPm;
    private String postingUser;
    private String content;
    DataBase dataBase = DataBase.getinstance();


    public Notification(boolean isPm, String postingUser, String content)
    {
        this.isPm = isPm;
        this.postingUser = postingUser;
        this.content = content;
    }

    @Override
    public void process(int connectionId, Connections connections) {}

    @Override
    public short getOpCode()
    {
        return OpCode;
    }

    public boolean isPM() {
        return isPm;
    }

    public String getPostingUser() {
        return postingUser;
    }

    public String getContent() {
        return content;
    }
}
