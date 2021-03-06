package bgu.spl.net.api;
import bgu.spl.net.Commands.*;
import bgu.spl.net.Commands.Command;
import bgu.spl.net.PassiveObjects.User;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.LogManager;

import java.io.Serializable;

public class StompMessagingProtocolImpl<T> implements StompMessagingProtocol<Serializable> {
    private User user;
    private int connectionid;
    private ConnectionsImpl connections ;
    private boolean terminate;
    private LogManager logM = LogManager.getInstance();


    public StompMessagingProtocolImpl( ){
    }

    public void start(int connectionId, ConnectionsImpl connections) {
        this.connectionid = connectionId;
        this.connections = (ConnectionsImpl) connections;

    }

    @Override
    public void process(Serializable message) {
        User user = connections.getClientByMsg((Message) message);
        Message msg = (Message) message;
        switch (msg.getType()){
            case CONNECT: {
                Command a = new Login(msg);
                Message toSend = a.execute(user);
                connections.send(user.getConnectionId(),toSend);
                logM.log.info("Sent msg to " + user.getConnectionId() + " Msg: " + '\n' + toSend);
                if (toSend.getCommand() == "ERROR"){
                    connections.disconnect(user.getConnectionId());
                }
            }

                break;
            case SUBSCRIBE: {
                Command c = new JoinGenre(msg);
                Message toSend =  c.execute(user);
                connections.send(user.getConnectionId(),toSend);
                logM.log.info("Sent msg to " + user.getConnectionId() + " Msg: " + '\n' + toSend);
                break;
            }
            case SEND: {
                //System.out.println("SEND");
                if (msg.getBody().toLowerCase().indexOf("added") > 0) { //addBookCase
                    Command c = new AddBook(msg);
                    Message toSend =  c.execute(user);
                    connections.send(msg.getHeader().get(0).getSecond(),toSend);
                }
                else if (msg.getBody().toLowerCase().indexOf("returning") >= 0) { //ReturnBookCase
                    Command c = new ReturnBook(msg);
                    Message toSend =  c.execute(user);
                    connections.send(msg.getHeader().get(0).getSecond(),toSend);
                }
                else if (msg.getBody().toLowerCase().indexOf("book status") >= 0) { //GenreBookStatusCase
                    String genre = msg.getHeader().get(0).getSecond();
                    Command c = new GenreBookStatus(msg);
                    Message toSend =  c.execute(user);
                    connections.send(genre, toSend);
                }

                else if (msg.getBody().toLowerCase().indexOf(user.getName()+":") >= 0){ //GenreBookStatusResponseCase
                    String genre = msg.getHeader().get(0).getSecond();
                    Command c = new GenreBookStatusResponse(msg);
                    Message toSend =  c.execute(user);
                    connections.send(genre, toSend);
                }
                else if (msg.getBody().toLowerCase().indexOf("wish") > 0){ //BorrowBookCase
                    String genre = msg.getHeader().get(0).getSecond();
                    Command c = new BorrowBook(msg);
                    Message toSend =  c.execute(user);
                    connections.send(genre, toSend);
                }
                else if (msg.getBody().toLowerCase().indexOf("has") > 0){ //BorrowBookResponseCase
                    String genre = msg.getHeader().get(0).getSecond();
                    Command c = new BorrowBookResponse(msg);
                    Message toSend =  c.execute(user);
                    connections.send(genre, toSend);
                }

                else if (msg.getBody().toLowerCase().indexOf("taking") >= 0){ //LoaningBookCase
                    String genre = msg.getHeader().get(0).getSecond();
                    Command c = new LoaningBook(msg);
                    Message toSend =  c.execute(user);
                    connections.send(genre, toSend);
                }
                break;
            }
            case DISCONNECT:{
                Command c = new Logout(msg);
                Message toSend =  c.execute(user);
                connections.send(user.getConnectionId(),toSend);
                terminate=true;
            }
                break;
            case UNSUBSCRIBE: {
                Command c = new ExitGenre(msg);
                Message toSend =   c.execute(user);
                connections.send(user.getConnectionId(),toSend);
                break;
            }
            case MESSAGE:
                break;
            case RECEIPT:
                break;
            case ERROR:
                break;
        }
    }



    @Override
    public boolean shouldTerminate() {
        if (this.terminate ==true){
            connections.disconnect(connectionid);
        }
        return this.terminate;
    }




}
