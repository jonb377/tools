package com.bolin.exceptions;

/**
 * Created by jonb3_000 on 7/19/2016.
 */
public class ConnectionException extends RuntimeException {

    public ConnectionException(String why) {
        super(why);
    }

}
