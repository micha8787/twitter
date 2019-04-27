//
// Created by shafirmi@wincs.cs.bgu.ac.il on 1/3/19.
//

#ifndef ENCODERDECODER_H
#define ENCODERDECODER_H


#include <string>
#include<vector>
class EncoderDecoder {

public:
    EncoderDecoder();
    int bytescounter;
    void encoderFromKeyBoard(std::string line , char* bytes);
    void decoder();
     //std::string split(std::string &s, char delimiter); DIDNT WORK, IDK WHY BUT I TOOK ANOTHER SPLIT FUNCTION FROM THE INTERNET
     void registerOrLogin(std:: vector<std::string>, char *bytes);
    void insertopcode(short i);
    std::vector <std::string> splitToVector(std::string &s, char delimiter);
    void shortToBytes(short num, char* bytesArr);
    void  pushToBytes(char cha,char* bytes);
    void addWord(std::string str,char* bytes);
    short bytesToShort(char* bytesArr);

    int getBytescounter() ;
};


#endif ENCODERDECODER_H
