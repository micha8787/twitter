package bgu.spl.net.srv.Messages;



import bgu.spl.net.api.Message;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.DataBase;

public class ErrorMessage implements Message {
    private short OpCode=11;
    private short MessageOpCode;
    DataBase dataBase=DataBase.getinstance();
    public ErrorMessage(short messageOpCode)
    {
        MessageOpCode = messageOpCode;
    }

    @Override
    public void process(int connectionId, Connections connections) {}

    @Override
    public short getOpCode() {
        return OpCode;
    }

    public short getMessageOpCode() {
        return MessageOpCode;
    }

}

