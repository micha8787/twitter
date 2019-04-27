package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.BgsMessageProtocol;
import bgu.spl.net.srv.EncoderDecoder;
import bgu.spl.net.srv.Server;

public class TPCMain
{


    public static void main(String[] args)
    {
        int port =  Integer.parseInt(args[0]);;
        Server server = Server.threadPerClient(port, () -> new BgsMessageProtocol(), () -> new EncoderDecoder());
        server.serve();
    }


}
