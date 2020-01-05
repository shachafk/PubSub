package bgu.spl.net.api;


import java.util.concurrent.atomic.AtomicInteger;

public  class MessageID {
    private static AtomicInteger messageId =new AtomicInteger(0);

    public static int getMessageId() {
       return messageId.incrementAndGet();
    }
}
