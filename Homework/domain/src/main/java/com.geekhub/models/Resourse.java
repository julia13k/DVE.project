package com.geekhub.models;

import java.io.Serializable;
import java.util.Optional;

public class Resourse implements Serializable {
    private String name;
    private ResourseType type;
    private String data;
    private static final long serialVersionUID = 1L;

    public Resourse(String name, String data, ResourseType type) {
        this.name = name;
        this.data = data;
        this.type = type;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<ResourseType> getType() {
        return Optional.ofNullable(type);
    }

    public void setType(ResourseType type) {
        this.type = type;
    }

    public Optional<String> getData() {
        return Optional.ofNullable(data);
    }

    public void setData(String data) {
        this.data = data;
    }

}
