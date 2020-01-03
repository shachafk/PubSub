package bgu.spl.net.Commands;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.messagebroker.Client;
import bgu.spl.net.messagebroker.MessageBroker;
import bgu.spl.net.srv.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Login implements Command {
    MessageBroker msgBrokerInstance=MessageBroker.getInstance();
    ConnectionImpl connection;
    private LogManager logM = LogManager.getInstance();

    Login(ConnectionImpl connection){
        this.connection=connection;
    }


    @Override
    public Serializable execute(Object arg) {
        if (arg instanceof Client) {
            Client client = (Client) arg;
            Integer connectionId = client.getConnectionId();

           HashMap<Integer, ConnectionHandler> activeUsers =connection.getActiveUsers();
            if(activeUsers.containsKey(client.getConnectionId())){ //checks weather client already logged in

                logM.log.severe("client"+client.getName()+"already logged in");

            }
            else{ //creats new client, send receipt
                Client newClient=new Client(connectionId, client.getName(),client.getPassword());
                msgBrokerInstance.getRegistered().put(client,new ConcurrentLinkedQueue<String>());
                //here need to send CONNECTED frame back to client
                logM.log.info("client"+client.getName()+"created and added to Registered");
            }
        }
        else {
           logM.log.severe("arg isnt instance of Client");
        }
        return null;
  }
}
