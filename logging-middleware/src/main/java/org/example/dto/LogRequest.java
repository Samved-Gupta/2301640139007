package org.example.dto;

public class LogRequest {
    private String stack;
    private String level;
    private String pkg;
    private String message;

    // No-argument constructor
    public LogRequest() {
    }

    // All-arguments constructor that the compiler was missing
    public LogRequest(String stack, String level, String pkg, String message) {
        this.stack = stack;
        this.level = level;
        this.pkg = pkg;
        this.message = message;
    }

    // Getters and Setters
    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
