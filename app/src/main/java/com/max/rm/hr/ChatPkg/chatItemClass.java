package com.androidmax.max.hr.ChatPkg;

public class chatItemClass extends chatInfo {
   // String name;
    String time;
    String lastMessage;
    String icon;
    String chat_id;
    String countItem;

    public String getCountItem() {
        return countItem;
    }

    public void setCountItem(String countItem) {
        this.countItem = countItem;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public chatItemClass() {
    }

    String emp_id;

    @Override
    public String toString() {
        return "chatItemClass{" +
                "time='" + time + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", icon='" + icon + '\'' +
                ", chat_id='" + chat_id + '\'' +
                ", emp_id='" + emp_id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", Description='" + Description + '\'' +
                '}';
    }

    public chatItemClass(String type, String name, String Description, String createdBy, String createdAt, String time, String lastMessage, String icon, String chat_id, String emp_id) {
        super(type, name, Description, createdBy, createdAt);
        this.time = time;
        this.lastMessage = lastMessage;
        this.icon = icon;
        this.chat_id = chat_id;
        this.emp_id = emp_id;
    }

    public chatItemClass(String name, String time, String lastMessage, String icon, String chat_id, String emp_id) {
        this.name = name;
        this.time = time;
        this.lastMessage = lastMessage;
        this.icon = icon;
        this.chat_id= chat_id;
       this. emp_id=emp_id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getChat_id() {
        return chat_id;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getIcon() {
        return icon;
    }
}
