package bgu.spl.net.srv.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.DataBase;

public class Ack  implements Message {
    private short OpCode=10;
    private short messageOpCode;
    private String content;
    private short numofusers, numPosts, numFollowers, numFollowing;
    DataBase dataBase=DataBase.getinstance();

    public Ack(short messageOpCode)
    {
        this.messageOpCode = messageOpCode;
    }

    public Ack(short messageOpCode, String content)
    {
        messageOpCode = messageOpCode;
        this.content = content;
    }

    public Ack(short numofusers, short messageOpCode, String content) {
        this.numofusers = numofusers;
        this.messageOpCode = messageOpCode;
        this.content = content;
    }

    public Ack(short numPosts, short numFollowers, short numFollowing)
    {
        this.numPosts = numPosts;
        this.numFollowers = numFollowers;
        this.numFollowing = numFollowing;

    }

    public short getNumPosts() {
        return numPosts;
    }

    public short getNumFollowers() {
        return numFollowers;
    }

    public short getNumFollowing() {
        return numFollowing;
    }

    @Override
    public void process(int connectionId, Connections connections) {}

    @Override
    public short getOpCode() {
        return OpCode;
    }

    public short getNumofusers() {
        return numofusers;
    }

    public short getMessageOpCode() {
        return messageOpCode;
    }

    public String getContent() {
        return content;
    }
}
