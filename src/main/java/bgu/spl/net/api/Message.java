package bgu.spl.net.api;
import bgu.spl.net.Commands.CommandType;
import bgu.spl.net.srv.Pair;

import java.awt.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Iterator;
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
    private String genre;
    private int subId;
    private int receiptId;
    private String userName;
    private String password;
    private String host;
    private String version;



    public Message() {
        header = new LinkedList<Pair<String,String>>();
    }

    public Message(Message from){

       this.command = from.command;
        this.header = from.header;
        this.body = from.body;
        this.emptyLine = from.emptyLine;
        this.endOfMsg = from.endOfMsg;
        this.index = from.index;
        this.type = from.type;
        this.genre = from.genre;
        this.subId = from.subId;
        this.receiptId = from.receiptId;
        this.userName = from.userName;
        this.password = from.password;
        this.host = from.host;
        this.version = from.version;
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
        loadHeaders();
        return endOfMsg;
    }
    public void loadHeaders() {
        Iterator it = header.iterator();
        while (it.hasNext()) {
            Pair curr = (Pair) it.next();
            switch ((String) curr.getFirst()) {
                case ("destination"):
                    genre = (String) curr.getSecond();
                    break;
                case ("id"):
                    subId = Integer.valueOf((String) curr.getSecond());
                    break;
                case ("receipt"):
                    receiptId = Integer.valueOf((String) curr.getSecond());
                    break;
                case ("host"):
                    host = (String) curr.getSecond();
                    break;
                case ("login"):
                    userName = (String) curr.getSecond();
                    break;
                case ("passcode"):
                    password = (String) curr.getSecond();
                    break;
                case ("accept-version"):
                    version = (String) curr.getSecond();
                    break;
            }
        }
    }

    public int getReceiptId() {
        return receiptId;
    }

    public int getIndex() {
        return index;
    }

    public int getSubId() {
        return subId;
    }

    public String getGenre() {
        return genre;
    }

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public String getVersion() {
        return version;
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

    public String toStringError(){
        String toReturn = "";
        toReturn = toReturn + command + System.lineSeparator();
        for (Pair p : header){
            String curr = p.getFirst() + ":" + p.getSecond();
            toReturn = toReturn + curr + System.lineSeparator();
        }
        toReturn = toReturn + System.lineSeparator();
        toReturn = toReturn + body + System.lineSeparator();

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

    public void clear() {
        command ="";
        header.clear();
        body="";
        emptyLine = false;
        endOfMsg = false;
        index = 0;
        type = null;
    }
}
