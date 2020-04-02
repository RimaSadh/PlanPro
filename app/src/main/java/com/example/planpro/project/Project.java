package com.example.planpro.project;

import com.google.firebase.Timestamp;

public class Project {

    private String Id;
    private String name, description, projectManager;
    private Timestamp startDate, endDate;
    private double totalCost;

    public Project(){}

    public Project(String id, String name, String description, String projectManager, Timestamp startDate, Timestamp endDate, double totalCost ){
        this.Id = id;
        this.name = name;
        this.description = description;
        this.projectManager = projectManager;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
    }
    public Project(String name, String description, Timestamp startDate, Timestamp endDate ){

        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;

    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

}
