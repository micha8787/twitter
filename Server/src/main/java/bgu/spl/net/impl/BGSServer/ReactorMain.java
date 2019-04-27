package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.BgsMessageProtocol;
import bgu.spl.net.srv.EncoderDecoder;
import bgu.spl.net.srv.Server;

public class ReactorMain
{
    public static void main(String args[])
    {
        final int port = Integer.parseInt(args[0]);
        final int threadNum = Integer.parseInt(args[1]);

        Server server =Server.reactor(threadNum,port,() -> new BgsMessageProtocol(), () -> new EncoderDecoder());
        server.serve();
        //register post and pm
    }
}
