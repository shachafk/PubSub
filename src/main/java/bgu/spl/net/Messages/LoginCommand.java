package bgu.spl.net.Messages;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.messagebroker.Client;
import bgu.spl.net.messagebroker.MessageBroker;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.ConnectionImpl;
import bgu.spl.net.srv.LogManager;


import java.io.Serializable;

import java.util.HashMap;

public class LoginCommand implements Command {
    MessageBroker msgBrokerInstance=MessageBroker.getInstance();
    ConnectionImpl connection;
    private LogManager logM = LogManager.getInstance();

    LoginCommand(ConnectionImpl connection){
        this.connection=connection;
    }


    @Override
    public Serializable execute(Object arg) {
        if (arg instanceof Client) {
            Client client = (Client) arg;
            Integer connectionId = client.getConnectionId();
            HashMap<Integer, ConnectionHandler> activeUsers =connection.getActiveUsers();
            if(activeUsers.containsKey(client.getConnectionId())){

            }
            else{
             //   connection.getActiveUsers()
            }
        }
        else {
           logM.log.severe("arg isnt instance of Client");
        }
        return null;
  }
}
