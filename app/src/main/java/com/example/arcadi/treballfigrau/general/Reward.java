package com.example.arcadi.treballfigrau.general;

public class Reward {
    private int Id,Points;
    private String Name,Description;

    public Reward(Integer id1, String name, Integer points, String description) {
        this.Id = id1;
        this.Name = name;
        this.Points = points;
        this.Description = description;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
