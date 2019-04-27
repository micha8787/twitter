//
// Created by shafirmi@wincs.cs.bgu.ac.il on 1/3/19.
//

#ifndef SENDTOSERVER_H
#define SENDTOSERVER_H

#include "ConnectionHandler.h"

class SendToServer{
private:
    ConnectionHandler & connectionHandler;
    bool *loggedout ;
    bool *flag ;
public:
    SendToServer(ConnectionHandler & connectionHandler1,bool *loggedout,bool *flag);
    void run();

};


#endif SENDTOSERVER_H
