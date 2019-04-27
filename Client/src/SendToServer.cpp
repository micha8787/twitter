//
// Created by shafirmi@wincs.cs.bgu.ac.il on 1/3/19.
//
# include "../include/SendToServer.h"
#include "../include/EncoderDecoder.h"


SendToServer::SendToServer(ConnectionHandler &connectionHandler,bool *loggedout,bool *flag) : connectionHandler(connectionHandler),loggedout(loggedout),flag(flag){

}

void SendToServer::run() {
while(!*loggedout)
{
    const short bufsize = 1024;
    char buf[bufsize];
    std::cin.getline(buf, bufsize);
    std::string line(buf);
    int len = line.length();
    char bytes[bufsize];

    EncoderDecoder e;
    e.encoderFromKeyBoard(line, bytes);
    int size = e.getBytescounter();

    if (!connectionHandler.sendBytes(bytes, size)) {

        std::cout << "Disconnected. Exiting...\n" << std::endl;
        //  break;
    }
    // connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
   // std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;
    if(line=="LOGOUT"){
        while(*flag){}
        *flag=true;
    }
}

}
