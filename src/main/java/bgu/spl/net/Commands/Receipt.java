package bgu.spl.net.Commands;

import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class Receipt implements Command {
    private int receiptid;
    private String body;

    public Receipt(int receiptid,String body){
        this.receiptid = receiptid;
        this.body = body;
    }

    @Override
    public Serializable execute(Object arg) {
        return null;
    }
}
