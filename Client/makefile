CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude

LDFLAGS:=-lboost_system -pthread



all: Client

	g++ -o bin/BGSclient bin/ConnectionHandler.o bin/Client.o bin/SendToServer.o bin/EncoderDecoder.o $(LDFLAGS)



Client: bin/ConnectionHandler.o bin/Client.o bin/SendToServer.o bin/EncoderDecoder.o

	

bin/ConnectionHandler.o: src/ConnectionHandler.cpp

	g++ $(CFLAGS) -o bin/ConnectionHandler.o src/ConnectionHandler.cpp



bin/Client.o: src/Client.cpp

	g++ $(CFLAGS) -o bin/Client.o src/Client.cpp



bin/SendToServer.o: src/SendToServer.cpp

	g++ $(CFLAGS) -o bin/SendToServer.o src/SendToServer.cpp 



bin/EncoderDecoder.o: src/EncoderDecoder.cpp

	g++ $(CFLAGS) -o bin/EncoderDecoder.o src/EncoderDecoder.cpp 




.PHONY: clean

clean:

	rm -f bin/*
