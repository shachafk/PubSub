package bgu.spl.net.Commands;

import bgu.spl.net.PassiveObjects.User;
import bgu.spl.net.api.Message;

import java.io.Serializable;

public interface Command<T> extends Serializable {


    Message execute(User arg);

    CommandType getType();
}
