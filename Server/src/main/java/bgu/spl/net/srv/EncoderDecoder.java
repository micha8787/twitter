package bgu.spl.net.srv;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.Messages.*;

import java.lang.Error;
import java.util.Arrays;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;





public class EncoderDecoder implements MessageEncoderDecoder<Message> {



    private final ByteBuffer opcodeBuffer = ByteBuffer.allocate(2);//this should save the 2 1st bytes which represent the opcode
    private byte[] bytes = new byte[1 << 10]; //start with 1k- this is saving the message itself
    private int len = 0; //the length of the bytes array
    private  Short OpCode = 0;
    Message message;
    int numOfZerosThatShouldBe;//the num of zeros in all the message
    int zerosCounter = 0;
    int counter = 0;


   public Message decodeNextByte(byte nextByte)
    {
        if (opcodeBuffer.remaining() == 2)
        {
          //  System.out.println("remaining 2");
            opcodeBuffer.put(nextByte);

            return null;
        }

        else if (opcodeBuffer.remaining() == 1)
        {
          //  System.out.println("remaining 1");
            opcodeBuffer.put(nextByte);
            //System.out.println(opcodeBuffer.remaining());
            opcodeBuffer.flip();
            OpCode = opcodeBuffer.getShort();
           // System.out.println("OPCODE:"+ OpCode);
            if (OpCode == 3)
            {
                clearall();
                return new LogoutRequest();
            }
            else if (OpCode == 7)
            {
                clearall();
                return new UserListRequest();
            }
            return null;//we have the 1st 2 bytes of the message and it doesnt finished
        }
        else
            switch (OpCode)
            {
                case 1:
                    numOfZerosThatShouldBe = 2;
                    if (nextByte != 0)// the message didnt finished yet
                    {

                        pushByte(nextByte);
                        return null;
                    }
                    else// the message is finished
                    {

                        zerosCounter++;
                        if (zerosCounter < numOfZerosThatShouldBe)
                        {
                            String username = popString();
                            clearbytes();
                            message = new RegisterRequest(username);

                            return null;
                        }

                        String password = popString();// if we got to the last 0
                        clearbytes();
                        ((RegisterRequest) message).setPassword(password);
                        clearall();

                        return message;
                    }
                case 2:

                    numOfZerosThatShouldBe = 2;
                    if (nextByte != 0)// the message didnt finished yet
                    {
                        pushByte(nextByte);
                        return null;
                    }
                    else// the message is finished
                    {
                        zerosCounter++;
                        if (zerosCounter < numOfZerosThatShouldBe) {
                            String username = popString();
                            clearbytes();
                            message = new LoginMessage();
                            ((LoginMessage) message).setUsername(username);
                            return null;
                        }
                        String password=popString();// if we got to the last 0
                        clearbytes();
                        ((LoginMessage) message).setPassword(password);
                        clearall();

                        return message;
                    }
                case 4:
                    ////THE PROBLEM IS HERE:
                    if(counter < 3 )
                    {
                        counter ++;
                        pushByte(nextByte);
                        return null;

                    }
                    else if(counter == 3)
                        {

                            boolean followOrUnfollow = (bytes[0] == 0);
                            ByteBuffer tmp = ByteBuffer.allocate(2);
                            tmp.put(bytes, 1 ,2 );
                            tmp.flip();
                            numOfZerosThatShouldBe = tmp.getShort();
                            counter ++;
                             message = new FollowRequest(followOrUnfollow,numOfZerosThatShouldBe);
                             clearbytes();
                             //changed:
                             len=0;
                             //end
                            if (nextByte != 0)
                            {
                                pushByte(nextByte);
                                return null;
                            }
                            pushByte(nextByte);// could be problematic
                            return null;// could be problematic


                        }
                    else // we finished the 1st 3 bytes and now we are at the names of the users
                        {
                            if (nextByte != 0)  // the message didnt finished yet
                            {
                                pushByte(nextByte);
                                counter++;
                                return null;
                            }
                            else
                            {
                                counter++;
                                zerosCounter++;
                                String name =popString();
                                //String name =new String(bytes, 3, counter-4, StandardCharsets.UTF_8);
                                //len=0;
                                clearbytes();
                                ((FollowRequest)message).AddToUserNameList(name);
                                if (zerosCounter==numOfZerosThatShouldBe)
                                {
                                    clearall();
                                    return message;
                                }

                                return null;
                            }
                        }

                case 5:
                        if (nextByte==0)
                        {
                            String content = popString();
                            message=new PostRequest(content);
                            clearall();
                            return message;
                        }
                        pushByte(nextByte);
                        return null;
                case 6:
                        if (nextByte!=0)
                        {
                            pushByte(nextByte);
                            return null;
                        }
                        zerosCounter++;
                        if (zerosCounter<2)
                        {
                            String username=popString();
                            clearbytes();
                            message = new PMRequest(username);
                            return null;
                        }
                         String content=popString();
                        ((PMRequest) message).setContent(content);
                        clearall();
                        return message;
                case 8:
                    if (nextByte!=0)
                    {
                    pushByte(nextByte);
                    return null;
                    }
                    String username = popString();
                    message     = new StatsRequest(username);
                    clearall();
                    return message;

            }
        return null;
    }

    public byte[] encode(Message message1)
    {

        short mesopcode = message1.getOpCode();
        byte[] zero = new byte[1];
        zero[0] = 0;
        byte [] opcodebyte = shortToBytes(mesopcode);
        byte[] result;
        switch (mesopcode)
        {
            case 9:
                byte[] ispmbyte = new byte[1];
                    if(((Notification)message1).isPM())
                    {
                        ispmbyte[0] = 0;
                    }
                    else
                    {
                        ispmbyte[0] = 1;
                    }
                result = merge(opcodebyte,ispmbyte);
                String postingUser = ((Notification)message1).getPostingUser();
                byte[] positngUserBytes = postingUser.getBytes();
                result = merge(result,positngUserBytes);
                result = merge(result,zero);
                String content =((Notification)message1).getContent();
                result = merge(result,content.getBytes());
                result = merge(result,zero);
                return result;

            case 10:

                short messageOpcode = ((Ack)message1).getMessageOpCode();
                byte[] bytesmessageopcode = shortToBytes(messageOpcode);
                result = merge(opcodebyte,bytesmessageopcode);
                    if (messageOpcode == 4|messageOpcode == 7)
                    {

                        short numOfUsers = ((Ack)message1).getNumofusers();
                        byte[] numOfUsers1 = shortToBytes(numOfUsers);
                        result=merge(result,numOfUsers1);
                        String optional = ((Ack)message1).getContent();
                        byte [] encodedOptional = optional.getBytes();
                        result = merge(result,encodedOptional);
                    }
                    else if(messageOpcode == 8)
                    {
                        short numOfPosts = ((Ack)message1).getNumPosts();
                        short numOffollowers = ((Ack)message1).getNumFollowers();
                        short numOffollowing = ((Ack)message1).getNumFollowing();
                        byte [] bytenumOfPosts = shortToBytes(numOfPosts);
                        byte [] bytesFollowers = shortToBytes(numOffollowers);
                        byte [] bytesFollowing = shortToBytes(numOffollowing);
                        result = merge(result,bytenumOfPosts);
                        result = merge(result,bytesFollowers);
                        result = merge(result,bytesFollowing);

                    }
                    return result;


            case 11:
                byte[] bytemessageopcode=shortToBytes(((ErrorMessage)message1).getMessageOpCode());
                result=merge(opcodebyte,bytemessageopcode);
                return result;

        }
        return null;
    }

    public byte[] merge ( byte[] a,byte[] b)
    {
        int aLen = a.length;
        int bLen = b.length;
        byte[] c = new byte[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    public void clearall(){// eraseing everything before creating a new message
        opcodeBuffer.clear();
        OpCode=0;
        zerosCounter=0;
        len=0;
        numOfZerosThatShouldBe=0;
        counter=0;
        clearbytes();





    }

    private void pushByte(byte nextByte) {//implements dynamic array of bytes

       if (len >= bytes.length)
        {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }
    private void clearbytes()
    {
        bytes=new byte[1 << 10];
    }

}

/*   linemessageencoderdecoder from their implementation
public class LineMessageEncoderDecoder implements MessageEncoderDecoder<String> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    @Override
    public String decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte == '\n') {
            return popString();
        }

        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(String message) {
        return (message + "\n").getBytes(); //uses utf8 by default
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }
}






 */