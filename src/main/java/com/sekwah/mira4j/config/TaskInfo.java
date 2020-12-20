package com.sekwah.mira4j.config;

public class TaskInfo {
    public int taskId;
    public boolean isCompleted;
    
    public TaskInfo() {
        
    }
    
    public TaskInfo(int id, boolean completed) {
        this.taskId = id;
        this.isCompleted = completed;
    }
    
    public static TaskInfo of(int id, boolean completed) {
        return new TaskInfo(id, completed);
    }
}
