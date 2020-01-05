package bgu.spl.net.api;

import bgu.spl.net.srv.ConnectionsImpl;

import java.util.concurrent.atomic.AtomicInteger;

public  class MessageID {
    private static AtomicInteger messageId =new AtomicInteger(0);
    //private static class singletonHolder{ private static MessageID MessageIDInstance = new MessageID();}
    //public static MessageID getInstance() { return MessageID.singletonHolder.MessageIDInstance; }


    public static int getMessageId() {
       return messageId.incrementAndGet();
    }
}
