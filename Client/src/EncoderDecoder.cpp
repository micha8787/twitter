//
// Created by shafirmi@wincs.cs.bgu.ac.il on 1/3/19.
//
#include <sstream>
#include <iostream>
#include "../include/EncoderDecoder.h"

EncoderDecoder::EncoderDecoder()
{

}

void EncoderDecoder::encoderFromKeyBoard(std::string line, char* bytes)
{
    bytescounter=0;

    std::vector<std::string> words=splitToVector(line,' ');
   std:: string firstWord=words[0];

    //std::cout<< firstWord+ "  1ST WORD: ";
    //std::cout<<words[0];
    if(firstWord=="REGISTER")
    {
      //  std::cout<< "read the word register"<<std::endl;
        shortToBytes(1,bytes);
        registerOrLogin(words,bytes);

    }
    else if(firstWord=="LOGIN"){
        shortToBytes(2,bytes);
        registerOrLogin(words,bytes);
    }
    else if(firstWord=="LOGOUT"){
        shortToBytes(3,bytes);
    }

   else if(firstWord=="FOLLOW")
   {
        shortToBytes(4, bytes);

        if (words[1] == "0") {
            short followOrUn = 0;
            shortToBytes(0, bytes);
        } else {
            short followOrUn = 1;
            shortToBytes(1, bytes);
        }
        short numOfUsers = words.size()-3;//to get the number of users im counting the words and then -3 cause there is follow 0 3 at the beginning
        shortToBytes(numOfUsers, bytes);
        for (int i = 3; i < words.size(); i++) {
            addWord(words[i],bytes);
            pushToBytes('\0',bytes);
        }
    }
     else if (firstWord == "POST")
        {
            shortToBytes(5, bytes);
            std::string content = line.substr(5);//to get rid of the part of the "POST"
            addWord(content,bytes);
            pushToBytes('\0',bytes);

        }

     else if(firstWord=="PM")
        {
            shortToBytes(6, bytes);
            registerOrLogin(words,bytes);//BEHAVE LIKE REGISTER AND LOGIN
        }
     else if(firstWord=="USERLIST")
        {
            shortToBytes(7, bytes);
        }
     else if(firstWord=="STAT")
        {
            shortToBytes(8, bytes);
            addWord(words[1],bytes);
            pushToBytes('\0',bytes);
        }

    }



//std::string EncoderDecoder::split(std::string &s, char delimiter)
//{
//
//    for (int i = 0; i < s.size(); i++)
//    {
//        if (s[i] == delimiter)
//        {
//            return s.substr(0, i-1);
//        }
//    };



//}

//// to write clearcounter()-mike

void EncoderDecoder::pushToBytes(char cha,char* bytes) {
    bytes[bytescounter] = cha;
    bytescounter++;
}

void EncoderDecoder::addWord(std::string str,char* bytes) {
for(int i=0; i<str.size();i++)
{
    pushToBytes(str[i],bytes);
}
}



void EncoderDecoder::registerOrLogin(std::vector<std::string> word, char *bytes)
{
    addWord(word[1],bytes);
    pushToBytes('\0',bytes);
    addWord(word[2],bytes);
    pushToBytes('\0',bytes);
}

void EncoderDecoder::insertopcode(short i) {

}

void EncoderDecoder::shortToBytes(short num, char *bytesArr)
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
    this->bytescounter=this->bytescounter+2;
}

std::vector<std::string> EncoderDecoder::splitToVector(std::string &string, char delimiter) {
    std::vector<std::string> words;
    std::string token;
    std::istringstream tokenStream(string);
    while(getline(tokenStream,token,delimiter))
    {
        words.push_back(token);
    }
    return words;

}


int EncoderDecoder::getBytescounter()  {
    return bytescounter;
}

short EncoderDecoder::bytesToShort(char *bytesArr) {
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}


