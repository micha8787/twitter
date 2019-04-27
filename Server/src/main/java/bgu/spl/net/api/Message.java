package bgu.spl.net.api;

import bgu.spl.net.api.bidi.Connections;

public interface Message {

    public short getOpCode();
    public void process(int connectionId, Connections connections );

}
