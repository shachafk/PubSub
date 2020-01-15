package bgu.spl.net.Commands;

import bgu.spl.net.PassiveObjects.User;
import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageID;
import bgu.spl.net.srv.LogManager;

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
            int s=body.indexOf("Taking")+7;
            int f=body.indexOf("from");
            this.bookName=body.substring(s,f-1);

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
