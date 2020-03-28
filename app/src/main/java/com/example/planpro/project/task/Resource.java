package com.example.planpro.project.task;

public class Resource {

    private String Name;
    private int Cost;
    private String ID;

    public Resource(String name, int cost) {
        Name = name;
        Cost = cost;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCost() {
        return Cost;
    }

    public void setCost(int cost) {
        Cost = cost;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
