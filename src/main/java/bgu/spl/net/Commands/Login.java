package bgu.spl.net.Commands;

import bgu.spl.net.api.Message;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.PassiveObjects.User;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.LogManager;

import java.io.Serializable;

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
        if (arg instanceof User) {
            User user = (User) arg;
            Integer connectionId = user.getConnectionId();
            String username = user.getName();

            if (connections.getRegistered().containsKey(username)) { //checks weather user ever signed in
                if (connections.getLoggedIn().contains(username)) {
                    Message error = new Message();
                    error.setCommand("ERROR");
                    error.addHeader("receipt-id", "TBD reciptID");
                    error.addHeader("message", "TBD description");
                    error.setBody("The messeage:" + "User already logged in”");
                    logM.log.severe("user" + user.getName() + "already logged in");
                    return error;

                } else { // tbd add to loggedin
                    User tmp = connections.getRegistered().get(username);
                    String password = tmp.getPassword();
                    if (user.getPassword().equals(password)) {
                        Message success = new Message();
                        success.setCommand("CONNECTED");
                        success.addHeader("version", "1.2");
                        success.setBody("Login successful");
                        logM.log.info("user" + user.getName() + "logged in again succuesfully");
                        return success;

                    } else {
                        Message error = new Message();
                        error.setCommand("ERROR");
                        error.addHeader("receipt-id", "TBD reciptID");
                        error.addHeader("message", "TBD description");
                        error.setBody("Wrong Password");
                        logM.log.warning("user" + user.getName() + "passed wrong password");
                        return error;
                    }
                }


            } else {//creates new user, send receipt
                // User newClient = new User(connectionId, user.getName(), user.getPassword());
                connections.getRegistered().put(user.getName(), user);
                Message success = new Message();
                success.setCommand("CONNECTED");
                success.addHeader("version", "1.2");
                success.setBody("Login successful");
                logM.log.info("user" + user.getName() + "created and added to Registered");
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


