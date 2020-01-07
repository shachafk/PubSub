package bgu.spl.net.api;
import bgu.spl.net.Commands.*;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.PassiveObjects.User;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImpl;

import java.io.Serializable;

public class StompMessagingProtocolImpl<T> implements StompMessagingProtocol<Serializable> {
    private User user;
    private int connectionid;
    private ConnectionsImpl connections;
    private boolean terminate;

    public StompMessagingProtocolImpl( ){
    }

    public void start(int connectionId, Connections<String> connections) {
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
                Message toSend = (Message) a.execute(user);
                connections.send(user.getConnectionId(),toSend);
                System.out.println("CONNECT");
            }

                break;
            case SUBSCRIBE: {
                Command c = new JoinGenre(msg);
                Message toSend = (Message) c.execute(user);
                connections.send(user.getConnectionId(),toSend);
                System.out.println("SUBSCRIBE");
                break;
            }
            case SEND: {
                System.out.println("SEND");
                if (msg.getBody().indexOf("added") > 0) { //addBookCase
                    Command c = new AddBook(msg);
                    Message toSend = (Message)  c.execute(user);
                    connections.send(msg.getHeader().get(0).getSecond(),toSend);
                }
                else if (msg.getBody().indexOf("Returning") > 0) { //ReturnBookCase
                    Command c = new ReturnBook(msg);
                    Message toSend = (Message)  c.execute(user);
                    connections.send(msg.getHeader().get(0).getSecond(),toSend);
                }
                else if (msg.getBody().indexOf("book status") > 0) { //GenreBookStatusCase
                    String genre = msg.getHeader().get(0).getSecond();
                    Command c = new GenreBookStatus(msg);
                    Message toSend = (Message) c.execute(user);
                    connections.send(genre, toSend);
                }

                else if (msg.getBody().indexOf(user.getName()+":") > 0){ //GenreBookStatusResponseCase
                    String genre = msg.getHeader().get(0).getSecond();
                    Command c = new GenreBookStatusResponse(msg);
                    Message toSend = (Message) c.execute(user);
                    connections.send(genre, toSend);
                }
                else if (msg.getBody().indexOf("wish") > 0){ //BorrowBookCase
                    String genre = msg.getHeader().get(0).getSecond();
                    Command c = new BorrowBook(msg);
                    Message toSend = (Message) c.execute(user);
                    connections.send(genre, toSend);
                }
                else if (msg.getBody().indexOf("has") > 0){ //BorrowBookResponseCase
                    String genre = msg.getHeader().get(0).getSecond();
                    Command c = new BorrowBookResponse(msg);
                    Message toSend = (Message) c.execute(user);
                    connections.send(genre, toSend);
                }
                else if (msg.getBody().indexOf("Taking") > 0){ //LoaningBookCase
                    String genre = msg.getHeader().get(0).getSecond();
                    Command c = new LoaningBook(msg);
                    Message toSend = (Message) c.execute(user);
                    connections.send(genre, toSend);
                }
                break;
            }
            case DISCONNECT:{
                Command c = new Logout(msg);
                Message toSend = (Message) c.execute(user);
                connections.send(user.getConnectionId(),toSend);
                connections.disconnect(user.getConnectionId());
                System.out.println("DISCONNECT");
            }
                break;
            case UNSUBSCRIBE: {
                Command c = new ExitGenre(msg);
                Message toSend = (Message)  c.execute(user);
                connections.send(user.getConnectionId(),toSend);
                System.out.println("UNSUBSCRIBE");
                break;
            }
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
