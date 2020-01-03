package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocolImpl;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.messagebroker.MessageBroker;
import bgu.spl.net.srv.Server;

public class StompServer {

    public static void main(String[] args) {

        if (args.length != 1){
            System.out.println("Enter server mode as program argument");
        }
        else {
            switch (args[0].toLowerCase()){
                case "tpc":
                    System.out.println("Thread per client mode");
                    Server.threadPerClient(7777,new StompMessagingProtocolImpl<>(MessageBroker.getInstance()),ObjectEncoderDecoder::new).serve();
                    break;
                case "reactor":
                    System.out.println("Reactor mode");
                    break;
            }

        }

    }


}
