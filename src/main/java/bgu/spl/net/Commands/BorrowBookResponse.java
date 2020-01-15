package bgu.spl.net.Commands;

import bgu.spl.net.PassiveObjects.User;
import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageID;
import bgu.spl.net.srv.LogManager;

public class BorrowBookResponse implements Command {
    private LogManager logM = LogManager.getInstance();
    private CommandType type = CommandType.BorrowBookResponse;
    private String genre;
    private String bookName;
    private String loaner;
    private String body;

    public BorrowBookResponse(Message msg){
        if (!msg.getCommand().equals("SEND") | msg.getHeader().size() < 1) {
            logM.log.severe("BorrowBookResponse msg is not valid");
            return;
        } else {
            this.genre = msg.getGenre();
            this.body = msg.getBody();
            this.loaner=body.substring(0,body.indexOf(" "));
            this.bookName=body.substring(body.indexOf("has")+4);
        }
    }

    @Override
    public Message execute(User arg) {
        if (arg instanceof User) {
            User user = (User) arg;
            Message toReturn = new Message();
            toReturn.setCommand("MESSAGE");
            toReturn.addHeader("subscription", ""+ user.getSubscriptionIdPerTopic(genre));
            toReturn.addHeader("Message-id", ""+ MessageID.getMessageId());
            toReturn.addHeader("destination",""+genre);
            toReturn.setBody(""+loaner+" has "+bookName);
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
