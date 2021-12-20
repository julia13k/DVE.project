package models;

public class Resourse {
    private String name;
    private ResourseType type;
    private String data;

    public Resourse(String name, String data, ResourseType type) {
        this.name = name;
        this.data = data;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourseType getType() {
        return type;
    }

    public void setType(ResourseType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
