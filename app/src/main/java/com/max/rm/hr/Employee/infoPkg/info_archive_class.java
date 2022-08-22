package com.max.rm.hr.Employee.infoPkg;

public class info_archive_class {
    String name,image;
     int id;

    public int getId() {
        return id;
    }

    public info_archive_class(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public info_archive_class(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}

