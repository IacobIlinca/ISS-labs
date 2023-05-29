package service;

public class SpitalException extends Exception{
    public SpitalException(){}

    public SpitalException(String message){super(message);}

    public SpitalException(String message, Throwable cause) {super(message, cause);}
}
