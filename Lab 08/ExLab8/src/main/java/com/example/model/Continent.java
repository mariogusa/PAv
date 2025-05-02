package com.example.model;

public class Continent {
    private int id;
    private String name;

    public Continent() { }

    public Continent(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId()           { return id; }
    public void setId(int id)    { this.id = id; }
    public String getName()      { return name; }
    public void setName(String n){ this.name = n; }
}