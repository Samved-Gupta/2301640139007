package org.example.dto;

public class LogResponse {
    private String logID;
    private String message;

    // No-argument constructor
    public LogResponse() {
    }

    // Getters and Setters (getLogID was missing for the compiler)
    public String getLogID() {
        return logID;
    }

    public void setLogID(String logID) {
        this.logID = logID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
