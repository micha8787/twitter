    #include <stdlib.h>
#include <thread>
#include "../include/ConnectionHandler.h"
#include "../include/SendToServer.h"
#include "../include/EncoderDecoder.h"

    /**
    * This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
    */


    //// TO ADD BOOST IN THE MAKE FILE!!!!!!
    int main (int argc, char *argv[]) {
        std::string host = argv[1];
        short port =atoi(argv[2]);
        
        ConnectionHandler connectionHandler(host, port);

        if (!connectionHandler.connect()) {
            std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
            return 1;
        }

        //From here we will see the rest of the ehco client implementation:
        bool *loggedout=new bool(false);
        bool *flag=new bool(false);
        SendToServer sendToServer(connectionHandler,loggedout, flag);
        std::thread th1(&SendToServer::run, &sendToServer);
        while (1) {


            // We can use one of three options to read data from the server:
            // 1. Read a fixed number of characters
            // 2. Read a line (up to the newline character using the getline() buffered reader
            // 3. Read up to the null character
            std::string answer;
            // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
            // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end

            char opCode[2];
            EncoderDecoder encoderDecoder;
            connectionHandler.getBytes(opCode,2);
            short opc=encoderDecoder.bytesToShort(opCode);//opc is the opcode we get from the server


                if (opc == 9)
                {
                    std::string toPrint = "";
                    std::string decodedMessage = "";
                    char follow[1];
                    connectionHandler.getBytes(follow, 1);
                    if (follow[0] == '\0')
                    {
                        toPrint.append("PM ");
                    }
                    else
                    {
                        toPrint.append("Public ");
                    }
                    connectionHandler.getFrameAscii(decodedMessage, '\0');
                    decodedMessage = decodedMessage.substr(0, decodedMessage.size() - 1);
                    toPrint.append(decodedMessage + " ");
                    decodedMessage = "";
                    connectionHandler.getFrameAscii(decodedMessage, '\0');
                    toPrint.append(decodedMessage);
                    toPrint = toPrint.substr(0, toPrint.size() - 1);

                    std::cout << toPrint << std::endl;

                }
                else if (opc == 10)
                {

                    char opcodeMsg[2];
                    std::string toPrint = "";
                    connectionHandler.getBytes(opcodeMsg, 2);
                    short opCMsg = encoderDecoder.bytesToShort(opcodeMsg);
                    if (opCMsg == 4 | opCMsg == 7)
                    {
                        std::string toPrint = "";
                        std::string decodedMsg = "";
                        char numOfUsers[2];
                        connectionHandler.getBytes(numOfUsers, 2);
                        int numberOfU = encoderDecoder.bytesToShort(numOfUsers);
                        toPrint.append(std::to_string(numberOfU));
                        connectionHandler.getFrameAscii(decodedMsg, '\0');
                        std::vector<std::string> followersToPrint = encoderDecoder.splitToVector(decodedMsg, '0');
                        for (int i = 0; i < followersToPrint.size(); i++)
                        {
                            toPrint.append(" " + followersToPrint[i]);

                        }
                        toPrint = toPrint.substr(0, toPrint.size() - 1);
                        std::cout << "ACK " +std::to_string(opCMsg) + toPrint << std::endl;

                    }
                    else if (opCMsg == 8)
                    {
                        char decodedMsg[6];
                        for (int i = 0; i < 3; i=i+1)
                        {
                            connectionHandler.getBytes(decodedMsg, 2);
                            short out = encoderDecoder.bytesToShort(decodedMsg);
                            toPrint.append(std::to_string(out) + " ");
                        }

                        std::cout << "ACK "+ std::to_string(opCMsg)+ " " + toPrint << std::endl;
                    }
                    else if (opCMsg == 3) {
                        std::cout << "ACK " + std::to_string(opCMsg)+ " " + toPrint << std::endl;

                        *loggedout=false;
                        *flag=false;
                        break;
                    }
                    else
                    {
                        std::cout << " ACK " +std::to_string(opCMsg) <<std:: endl;

                    }

                }
                else if (opc == 11)
                {
                    char opcodeMsg[2];
                    connectionHandler.getBytes(opcodeMsg, 2);
                    short opCMsg =   encoderDecoder.bytesToShort(opcodeMsg);
                    if (opCMsg == 3)
                    {
                        *flag=false;
                    }
                    std::cout <<"ERROR "+ std::to_string(opCMsg)<<std:: endl;
                }


//            if (!connectionHandler.getLine(answer)) {
//                std::cout << "Disconnected. Exiting...\n" << std::endl;
//               break;
//          }

            
           // len=answer.length();
            // A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
            // we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
           // answer.resize(len-1);
          //  std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
//            if (answer == "bye") {
//                std::cout << "Exiting...\n" << std::endl;
//                break;
//            }
        }
        return 0;
    }