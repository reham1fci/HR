package com.androidmax.max.hr.ChatPkg;

public class chatInfo {
    String type;
    String name;
    String createdBy;
    String createdAt;
    String Description;

    public chatInfo() {
    }

    @Override
    public String toString() {
        return "chatInfo{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", Description='" + Description + '\'' +
                '}';
    }

    public chatInfo(String type, String name, String Description, String createdBy, String createdAt) {

        this.type = type;
        this.name = name;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.Description = Description;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
