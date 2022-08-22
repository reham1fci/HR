package com.max.rm.hr.Admin;

import java.io.Serializable;

public class reminderClass implements Serializable {
    String task_id,taskDescription,taskTitle, date,taskTime,taskDate, time;
            int isRepeat;

    public reminderClass( String task_id, String taskTitle, String taskDescription, String date,String time, String taskTime, String taskDate, int isRepeat) {
        this.task_id = task_id;
        this.taskDescription = taskDescription;
        this.taskTitle = taskTitle;
        this.date = date;
        this.taskTime = taskTime;
        this.taskDate = taskDate;
        this.isRepeat = isRepeat;
        this.time=time;
    }


    public String getTime() {
        return time;
    }

    public String getTask_id() {
        return task_id;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getDate() {
        return date;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public int getIsRepeat() {
        return isRepeat;
    }
}
