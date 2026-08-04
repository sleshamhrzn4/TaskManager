package com.taskmanager.exception;

import java.util.Map;

public class ValidationException extends Exception{
    private final Map<String, String> fieldErrors;

    public ValidationException(Map<String, String> fieldErrors) {
        super("Validation Failed");
        this.fieldErrors = fieldErrors;
    }

    public ValidationException(String message){
        super (message);
        this.fieldErrors=null;

    }

    public Map<String, String> getFieldErrors(){
        return fieldErrors;
    }
}
