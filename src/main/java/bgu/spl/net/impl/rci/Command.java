package bgu.spl.net.impl.rci;

import bgu.spl.net.Commands.CommandType;

import java.io.Serializable;

public interface Command<T> extends Serializable {


    Serializable execute(T arg);

    CommandType getType();
}
