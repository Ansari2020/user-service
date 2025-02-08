package org.backend.userservice.Exceptions;

public class WrongPassword extends  RuntimeException{
    public WrongPassword(String message){
        super(message);
    }
}
