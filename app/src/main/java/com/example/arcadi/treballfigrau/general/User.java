package com.example.arcadi.treballfigrau.general;

public class User {
    private String Name,Surname, Email;
    private Integer Points, pid;


    public User(Integer pid, String name, String Surname, int Points, String Email) {
        this.pid = pid;
        this.Name = name;
        this.Surname = Surname;
        this.Points = Points;
        this.Email = Email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Integer getPoints() {
        return Points;
    }

    public void setPoints(Integer points) {
        Points = points;
    }
}
