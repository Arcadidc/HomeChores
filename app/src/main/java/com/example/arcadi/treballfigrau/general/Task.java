package com.example.arcadi.treballfigrau.general;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {
    private int Created_by, Assigned_to, Completed, Points, id;
    private String Name, Description;
    private Date date_creation, Date_end;
    private String created_by_name, Assigned_to_name;
    private Boolean Repetitive;
    private String[] days;


    private Boolean showMenu = false;

    public Task(){
    }

    public Task(Integer id1, String name, Integer points, String description) {
        this.id = id1;
        this.Name = name;
        this.Points = points;
        this.Description = description;
        this.Repetitive = false;
    }

    public int getCreated_by() {
        return Created_by;
    }

    public void setCreated_by(int created_by) {
        Created_by = created_by;
    }

    public int getAssigned_to() {
        return Assigned_to;
    }

    public void setAssigned_to(int assigned_to) {
        Assigned_to = assigned_to;
    }

    public int getCompleted() {
        return Completed;
    }

    public void setCompleted(int completed) {
        Completed = completed;
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

    public Date getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(Date date_creation) {
        this.date_creation = date_creation;
    }

    public Date getDate_end() {
        return Date_end;
    }

    public void setDate_end(Date date_end) {
        Date_end = date_end;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int Id) {
        id = Id;
    }

    public String getCreated_by_name() {
        return created_by_name;
    }

    public void setCreated_by_name(String created_by_name) {
        this.created_by_name = created_by_name;
    }

    public String getAssigned_to_name() {
        return Assigned_to_name;
    }

    public void setAssigned_to_name(String assigned_to_name) {
        Assigned_to_name = assigned_to_name;
    }

    public Boolean getRepetitive() {
        return Repetitive;
    }

    public void setRepetitive(Boolean repetitive) {
        Repetitive = repetitive;
    }

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }

    public Boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(Boolean showMenu) {
        this.showMenu = showMenu;
    }

}
