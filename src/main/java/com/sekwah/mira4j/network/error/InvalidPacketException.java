package com.sekwah.mira4j.network.error;

public class InvalidPacketException extends RuntimeException {
    private static final long serialVersionUID = -3039524274131809755L;
    
    public InvalidPacketException() {
        super();
    }
    
    public InvalidPacketException(String message) {
        super(message);
    }
}
