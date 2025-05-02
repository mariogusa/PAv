package com.example.model;

public class Country {
    private int id;
    private String name;
    private String code;
    private int continentId;

    public Country() { }

    public Country(int id, String name, String code, int continentId) {
        this.id          = id;
        this.name        = name;
        this.code        = code;
        this.continentId = continentId;
    }

    public int getId()                 { return id; }
    public void setId(int id)          { this.id = id; }
    public String getName()            { return name; }
    public void setName(String n)      { this.name = n; }
    public String getCode()            { return code; }
    public void setCode(String c)      { this.code = c; }
    public int getContinentId()        { return continentId; }
    public void setContinentId(int ci) { this.continentId = ci; }
}