package bgu.spl.net.Commands;

import bgu.spl.net.api.Message;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.messagebroker.Client;
import bgu.spl.net.srv.LogManager;

import java.io.Serializable;

public class ReturnBook implements Command {
    private String bookName;
    private String genre;
    private String user;
    private LogManager logM = LogManager.getInstance();
    private CommandType type = CommandType.ReturnBook;


    public ReturnBook(Message msg){
        if (!msg.getCommand().equals("SEND") | msg.getHeader().size() < 1) {
            logM.log.severe("ReturnBook msg is not valid");
            return;
        } else {
            this.genre = msg.getHeader().get(0).getSecond();
            String body = msg.getBody();
            this.bookName = body.substring(body.indexOf("Returning")+10,body.indexOf("to"));
            this.user = body.substring(body.indexOf("to") +3);
        }
    }


    @Override
    public Serializable execute(Object arg) {
        Client client = (Client) arg;

        return null;
    }

    @Override
    public CommandType getType() {
        return type;
    }
}
