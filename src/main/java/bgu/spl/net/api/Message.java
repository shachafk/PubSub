package bgu.spl.net.api;
import bgu.spl.net.Commands.CommandType;
import bgu.spl.net.srv.Pair;

import java.awt.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Message implements Serializable {
    private String command;
    private List<Pair<String,String>> header;
    private String body;
    private boolean emptyLine = false;
    private boolean endOfMsg = false;
    private int index = 0;
    private MessageType type;

    public Message() {
        header = new LinkedList<Pair<String,String>>();
    }

    public void addNextInput(String s){
        if (index==0){
            command =s;
            index++;
            // update the MessageType //
            if (command.equals("CONNECT"))
                this.type = MessageType.CONNECT;
            else if (command.equals("DISCONNECT"))
                this.type = MessageType.DISCONNECT;
            else if (command.equals("SEND"))
                this.type = MessageType.SEND;
            else if (command.equals("SUBSCRIBE"))
                this.type = MessageType.SUBSCRIBE;
            else if (command.equals("UNSUBSCRIBE"))
                this.type = MessageType.UNSUBSCRIBE;
        return;
        }
        if (s.equals("Finito")){
            endOfMsg=true;
        }
        else if (s.length()==0){
            emptyLine=true;
            body = "";
        }

        else if (emptyLine ==true){
            body = body +s;
            emptyLine=false;
        }

        else { // header
            int i = s.indexOf(":");
            String headerName = s.substring(0,i);
            String headerValue = s.substring(i+1);
            Pair tmp = new Pair(headerName,headerValue);
            header.add(tmp);
        }

    }
    public boolean isEndOfMsg(){
        return endOfMsg;
    }

    public List<Pair<String, String>> getHeader() {
        return header;
    }

    public String getCommand() {
        return command;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCommand(String command) {
        this.command = command;
    }
    public void addHeader(String headerName,String headerValue){
        Pair tmp = new Pair(headerName,headerValue);
        header.add(tmp);
    }

    public String toString(){
        String toReturn = "";
        toReturn = toReturn + command + System.lineSeparator();
        for (Pair p : header){
            String curr = p.getFirst() + ":" + p.getSecond();
            toReturn = toReturn + curr + System.lineSeparator();
        }
        toReturn = toReturn + System.lineSeparator();
        toReturn = toReturn + body + System.lineSeparator();
        toReturn = toReturn + "^@";

        return toReturn;
    }

    public static void main(String[] args) {
        Message msg = new Message();
        msg.setCommand("CONNECT");
        msg.addHeader("destination","78");
        msg.addHeader("version", "1.2");
        msg.setBody("logged in successfully ");
        System.out.println(msg.toString());
    }

    public MessageType getType() {
        return type;
    }
}
