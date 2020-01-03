package bgu.spl.net.Commands;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.messagebroker.Client;
import bgu.spl.net.messagebroker.MessageBroker;
import bgu.spl.net.srv.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Login implements Command {
    MessageBroker msgBrokerInstance = MessageBroker.getInstance();
    ConnectionImpl connection;
    private LogManager logM = LogManager.getInstance();

    Login(ConnectionImpl connection) {
        this.connection = connection;
    }


    @Override
    public Serializable execute(Object arg) {
        if (arg instanceof Client) {
            Client client = (Client) arg;
            Integer connectionId = client.getConnectionId();
            String username = client.getName();

            HashMap<Integer, ConnectionHandler> activeUsers = connection.getActiveUsers();
            if (activeUsers.containsKey(client.getConnectionId())) { //checks weather client already logged in
                Message error = new Message();
                error.setCommand("ERROR");
                error.addHeader("receipt-id", "TBD reciptID");
                error.addHeader("message", "TBD description");
                error.setBody("The messeage:" + "User already logged in”");
                logM.log.severe("client" + client.getName() + "already logged in");

                return error;

            } else {//creates new client, send receipt
                Client newClient = new Client(connectionId, client.getName(), client.getPassword());
                msgBrokerInstance.getRegistered().put(client.getName(), client);
                Message success = new Message();
                success.setCommand("CONNECTED");
                success.addHeader("version", "1.2");
                success.setBody("Login successful");
                logM.log.info("client" + client.getName() + "created and added to Registered");
                return success;
            }
/*            Client tmp = msgBrokerInstance.getRegistered().get(username);
            if (tmp != null) {
                String password = tmp.getPassword();
                if (client.getPassword().equals(password)) {
                    Message success = new Message();
                    success.setCommand("CONNECTED");
                    success.addHeader("version", "1.2");
                    success.setBody("Login successful");
                    logM.log.info("client" + client.getName() + "created and added to Registered");
                    return success;
                } else {
                    Message error = new Message();
                    error.setCommand("ERROR");
                    error.addHeader("receipt-id", "TBD reciptID");
                    error.addHeader("message", "TBD description");
                    error.setBody("Wrong Password");
                    //      logM.log.warning("client" + client.getName() + "passed wrong password");

                    return error;
                }
            }

        }*/

            // else {
            //       logM.log.severe("arg isn't instance of Client");
            //  }
            //  return null;
            // }
            //  }
        }
        return null;
    }
}

