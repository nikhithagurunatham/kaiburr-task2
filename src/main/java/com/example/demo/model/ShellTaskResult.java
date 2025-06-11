package com.example.demo.model;

public class ShellTaskResult {
    private String podName;
    private String message;
    private boolean success;

    public ShellTaskResult() {}

    public ShellTaskResult(String podName, String message, boolean success) {
        this.podName = podName;
        this.message = message;
        this.success = success;
    }

    public String getPodName() { return podName; }
    public void setPodName(String podName) { this.podName = podName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
