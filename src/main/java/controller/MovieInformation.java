package controller;

public class MovieInformation {
    String name;
    int year;
    boolean adult;

    public MovieInformation(String name, int year, boolean adult) {
        this.name = name;
        this.year = year;
        this.adult = adult;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public boolean isAdult() {
        return adult;
    }
}
