package com.example.planpro.project.task;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Task {

    private String ID;
    private String Name;
    private Timestamp Start;
    private Timestamp End;
    private int Cost;
    private ArrayList<Resource> resources;

    public Task(String name, int cost,Timestamp start, Timestamp end,  ArrayList<Resource> resources) {
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

    public Timestamp getStart() {
        return Start;
    }

    public void setStart(Timestamp start) {
        Start = start;
    }

    public Timestamp getEnd() {
        return End;
    }

    public void setEnd(Timestamp end) {
        End = end;
    }

    public int getCost() {
        return Cost;
    }

    public void setCost(int cost) {
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
