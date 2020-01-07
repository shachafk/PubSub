package bgu.spl.net.Commands;

import bgu.spl.net.PassiveObjects.User;
import bgu.spl.net.api.Message;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.LogManager;

import java.io.Serializable;

public class LoaningBook implements Command {
    private LogManager logM = LogManager.getInstance();
    private CommandType type = CommandType.LoaningBook;
    private String genre;
    private String body;
    private String bookName;
    private String lender;

    public LoaningBook(Message msg){
        if (!msg.getCommand().equals("SEND") | msg.getHeader().size() < 1) {
            logM.log.severe("AddBook msg is not valid");
            return;
        } else {
            this.genre = msg.getGenre();
            this.body = msg.getBody();
            this.lender=body.substring(body.indexOf("from")+5);
            this.bookName=body.substring(body.indexOf("Taking")+7);

        }
    }
    @Override
    public Serializable execute(Object arg) {
        if (arg instanceof User) {
            User user = (User) arg;
            Message toReturn = new Message();
            toReturn.setCommand("MESSAGE");
            toReturn.addHeader("destination",""+genre);
            toReturn.setBody("Taking "+bookName+" from "+lender);
            return toReturn;
        }
        else{
            logM.log.severe("arg isnt instance of User");
            return null;
        }
    }

    @Override
    public CommandType getType() {
        return type;
    }
}
