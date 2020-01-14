package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.LineMessageEncoderDecoder;
import bgu.spl.net.api.MessageID;
import bgu.spl.net.api.StompMessagingProtocolImpl;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.Server;

public class StompServer {

    public static void main(String[] args) {

        if (args.length < 2){
            System.out.println("Enter port and server mode as program argument");
        }
        else {
        int nThreads =Runtime.getRuntime().availableProcessors();
            //ConnectionsImpl conn = ConnectionsImpl.getInstance();
        int port = Integer.parseInt(args[0]);
        switch (args[1].toLowerCase()){
                case "tpc":
                    System.out.println("Thread per client mode");
                    Server.threadPerClient(port,
                            ()-> new StompMessagingProtocolImpl<>(),
                            ()-> new LineMessageEncoderDecoder<>()).serve();
                    break;
                case "reactor":
                    System.out.println("Reactor mode");
                    Server.reactor(nThreads,port,
                            ()-> new StompMessagingProtocolImpl<>(),
                            ()-> new LineMessageEncoderDecoder<>()).serve();
                    break;
            }

        }

    }


}
