package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BgsMessageProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.ConnectionsImpl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private Integer  connectionId;
    private Connections connections;



    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, BidiMessagingProtocol<T> protocol, Integer connectionId, Connections connections)
    {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.connectionId = connectionId;
        this.connections = connections;
    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read;
            //**********************************************************
            this.protocol.start(this.connectionId, this.connections);
            this.protocol.start(this.connectionId, this.connections);
            //((ConnectionsImpl) this.connections).getHandlersList().put(this.connectionId, this);
            //System.out.println(((ConnectionsImpl) this.connections).getHandlersList().size() + " sizeEEEEE");

            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                T nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null)
                {
                    protocol.process(nextMessage);
                    /*T response = protocol.process(nextMessage);
                    if (response != null) {  //their code
                        out.write(encdec.encode(response));
                        out.flush();
                    }*/
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException
    {
        connected = false;
        sock.close();
    }

    @Override
    public void send(T msg)
    {

        try {
            synchronized (this)
            {
            out.write(encdec.encode(msg));

            out.flush();
        }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
