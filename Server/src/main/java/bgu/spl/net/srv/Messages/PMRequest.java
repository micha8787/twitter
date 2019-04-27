package bgu.spl.net.srv.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.User;

public class PMRequest implements Message {
 private short OpCode=6;
 private String destUserName;
 private String Content;
 DataBase dataBase=DataBase.getinstance();
 public PMRequest(String userName)

 {
  destUserName = userName;
 }

 public PMRequest(String userName, String content)
 {
  destUserName = userName;
  Content = content;
 }

 @Override
 public void process(int connectionId, Connections connections)
 {
  String senderName = dataBase.getUserByConnectionId(connectionId).getUserName();
  Integer destId = dataBase.registeredUsers.get(this.destUserName).getConnectionid();

  if(dataBase.registeredUsers.get(this.destUserName) !=null)
  {
    Message msg = new Notification(true, senderName, this.Content);
    Message ack = new Ack(this.OpCode);

    if(dataBase.getUserByConnectionId(destId).isLoggedIn())
    {
         connections.send(connectionId,ack);
         connections.send(destId, msg);
    }
    else
     {
      connections.send(connectionId,ack);
      dataBase.getUserByConnectionId(destId).getmessagetosend().add(msg);
     }
  }

  else
  {
   connections.send(connectionId, new ErrorMessage(this.OpCode));
  }


 }



 public short getOpCode() {
  return OpCode;
 }

 public String getUserName() {
  return destUserName;
 }

 public String getContent() {
  return Content;
 }

 public void setContent(String content) {
  Content = content;
 }
}
