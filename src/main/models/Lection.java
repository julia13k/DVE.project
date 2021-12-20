package src.main.models;

public class Lection {
    private String name;
    private String description;
    private String date;
    private Resourse resource;
    private Person lecturer;
    private HomeWork homework;

    public Lection(String name, String description) {
        this.name = name;
        this.description = description;
        this.lecturer = lecturer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Resourse getResource() {
        return resource;
    }

    public void setResource(Resourse resource) {
        this.resource = resource;
    }

    public Person getLecturer() {
        return lecturer;
    }

    public void setLecturer(Person lecturer) {
        this.lecturer = lecturer;
    }

    public HomeWork getHomework() {
        return homework;
    }

    public void setHomework(HomeWork homework) {
        this.homework = homework;
    }
}
