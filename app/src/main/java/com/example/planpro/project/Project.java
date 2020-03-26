package com.example.planpro.project;

import java.sql.Timestamp;

public class Project {

    private int Id;
    private String name, description, projectManager;
    private Timestamp startDate, endDate;

    public Project(){}

    public Project(int id, String name, String description, String projectManager, Timestamp startDate, Timestamp endDate ){
        this.Id = id;
        this.name = name;
        this.description = description;
        this.projectManager = projectManager;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
