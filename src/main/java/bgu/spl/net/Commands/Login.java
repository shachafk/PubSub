package bgu.spl.net.Commands;

import bgu.spl.net.api.Message;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.PassiveObjects.User;
import bgu.spl.net.srv.ConnectionHandler;

import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.LogManager;
import java.io.Serializable;

public class Login implements Command {
    private LogManager logM = LogManager.getInstance();
    private Message msg;
    private CommandType type = CommandType.Login;
    private ConnectionsImpl connections = ConnectionsImpl.getInstance();
    private String username;
    private String password;
    private String version;


    public Login(Message msg) {
        this.msg = msg;
        username=msg.getUserName();
        password=msg.getPassword();
        version=msg.getVersion();
    }


    @Override
    public Serializable execute(Object arg) {
        if (arg instanceof User) {
            User user = (User) arg;
            String line="-----"+System.lineSeparator();
            Integer connectionId = user.getConnectionId();
            ConnectionHandler handler=connections.getActiveClients().get(connectionId);

            if (! (username.length() > 0 & password.length() > 0)){ // check if contains user & password
                Message error = new Message();
                error.setCommand("ERROR");
                error.addHeader("receipt-id", "TBD reciptID");
                error.addHeader("message", "malformed frame received");
                error.setBody("The message:"+System.lineSeparator()+line+msg.toStringError()+line +"malformed frame received");
                logM.log.severe("malformed frame received");
                return error;
            }


            if (connections.getRegistered().containsKey(username)) {//checks weather user ever signed in
                if (connections.getLoggedIn().containsKey(username)) {
                    Message error = new Message();
                    error.setCommand("ERROR");
                    error.addHeader("receipt-id", "TBD reciptID");
                    error.addHeader("message", "malformed frame received");
                    error.setBody("The message:"+System.lineSeparator()+line+msg.toStringError()+line +"User already logged in");
                    logM.log.severe("user" + user.getName() + "already logged in");
                    return error;

                }
                else {
                    User tmp = connections.getRegistered().get(username);
                    if (tmp != null) {
                        if (tmp.getPassword().equals(password)) {
                            user.setNameAndPass(username, password);
                            connections.addToLoggedIn(username, user);
                            return successfulMsg(user);
                        } else { //wrong password
                            Message error = new Message();
                            error.setCommand("ERROR");
                            error.addHeader("receipt-id", "TBD reciptID");
                            error.addHeader("message", "malformed frame received");
                            error.setBody("The message:" + System.lineSeparator() + line + msg.toStringError() + line + "Wrong Password");
                            logM.log.warning("user" + user.getName() + "passed wrong password");
                            //connections.disconnect(connectionId);
                            return error;
                        }
                    }
                }


            }
            else {//creates new user, send receipt
                if (user.isDefault()) {
                    user.setNameAndPass(username,password);
                    //User curr=new User(connectionId,username,password);
                    connections.getRegistered().put(username,user);
                    connections.getLoggedIn().put(user.getName(), user);
                    return successfulMsg(user);
                }
                else{
                    user.setNameAndPass(username,password);
                    connections.getLoggedIn().put(user.getName(), user);
                    return successfulMsg(user);
                }
            }



        }
        else {
            logM.log.severe("Object isnot instance of User");
        }
        return null;

    }

    public Message successfulMsg(User user){
        Message success = new Message();
        success.setCommand("CONNECTED");
        success.addHeader("version", ""+version);
        success.setBody("Login successful");
        logM.log.info("user " + user.getName() + " created and added to Registered");
        return success;
    }

    @Override
    public CommandType getType() {
        return type;
    }
}


