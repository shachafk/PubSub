package bgu.spl.net.Commands;

import bgu.spl.net.api.Message;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.messagebroker.Client;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.LogManager;

import java.io.Serializable;
import java.util.HashMap;

public class Login implements Command {
    private LogManager logM = LogManager.getInstance();
    private Message msg;
    private CommandType type = CommandType.Login;
    private ConnectionsImpl connections = ConnectionsImpl.getInstance();


    Login(Message msg) {
        this.msg = msg;
    }


    @Override
    public Serializable execute(Object arg) {
        if (arg instanceof Client) {
            Client client = (Client) arg;
            Integer connectionId = client.getConnectionId();
            String username = client.getName();

            if (connections.getRegistered().containsKey(username)) { //checks weather client ever signed in
                if (connections.getLoggedIn().contains(username)) {
                    Message error = new Message();
                    error.setCommand("ERROR");
                    error.addHeader("receipt-id", "TBD reciptID");
                    error.addHeader("message", "TBD description");
                    error.setBody("The messeage:" + "User already logged in”");
                    logM.log.severe("client" + client.getName() + "already logged in");
                    return error;

                } else { // tbd add to loggedin
                    Client tmp = connections.getRegistered().get(username);
                    String password = tmp.getPassword();
                    if (client.getPassword().equals(password)) {
                        Message success = new Message();
                        success.setCommand("CONNECTED");
                        success.addHeader("version", "1.2");
                        success.setBody("Login successful");
                        logM.log.info("client" + client.getName() + "logged in again succuesfully");
                        return success;

                    } else {
                        Message error = new Message();
                        error.setCommand("ERROR");
                        error.addHeader("receipt-id", "TBD reciptID");
                        error.addHeader("message", "TBD description");
                        error.setBody("Wrong Password");
                        logM.log.warning("client" + client.getName() + "passed wrong password");
                        return error;
                    }
                }


            } else {//creates new client, send receipt
                // Client newClient = new Client(connectionId, client.getName(), client.getPassword());
                connections.getRegistered().put(client.getName(), client);
                Message success = new Message();
                success.setCommand("CONNECTED");
                success.addHeader("version", "1.2");
                success.setBody("Login successful");
                logM.log.info("client" + client.getName() + "created and added to Registered");
                return success;
            }



        }
        return null;

    }

    @Override
    public CommandType getType() {
        return null;
    }
}


