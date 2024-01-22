package com.ices.aigccommunity.common;


public class AigcCommunityException extends RuntimeException {

    public AigcCommunityException() {
    }

    public AigcCommunityException(String message) {
        super(message);
    }

    /**
     * 丢出一个异常
     *
     * @param message
     */
    public static void fail(String message) {
        throw new AigcCommunityException(message);
    }

}
