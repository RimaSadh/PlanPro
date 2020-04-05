package com.example.planpro.project.task;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Task {

    private String ID;
    private String Name;
    private Timestamp Start;
    private Timestamp End;
    private double Cost;
    private ArrayList<Resource> resources;

    public Task(String id, String name, double cost,Timestamp start, Timestamp end,  ArrayList<Resource> resources) {
        ID = id;
        Name = name;
        Start = start;
        End = end;
        Cost = cost;
        this.resources = resources;
    }
    public Task(){
        //
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStart() {

        return new java.text.SimpleDateFormat("EEE, d MMM yyyy").format(Start.toDate());
    }

    public void setStart(Timestamp start) {
        Start = start;
    }

    public String getEnd() {

        return new java.text.SimpleDateFormat("EEE, d MMM yyyy").format(End.toDate());
    }

    public void setEnd(Timestamp end) {
        End = end;
    }

    public double getCost() {
        return Cost;
    }

    public void setCost(double cost) {
        Cost = cost;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void setResources(ArrayList<Resource> resources) {
        this.resources = resources;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
