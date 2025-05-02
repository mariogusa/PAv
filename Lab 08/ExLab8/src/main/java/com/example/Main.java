package com.example;

import com.example.dao.ContinentDAO;
import com.example.dao.CountryDAO;
import com.example.model.Continent;
import com.example.model.Country;

public class Main {
    public static void main(String[] args) {
        try {
            ContinentDAO contDao = new ContinentDAO();
            CountryDAO  ctryDao = new CountryDAO();

            // 1) Create a continent
            Continent europe = new Continent();
            europe.setName("Europe");
            contDao.create(europe);
            System.out.println("Created Continent: id=" + europe.getId());

            // 2) Create a country in that continent
            Country romania = new Country();
            romania.setName("Romania");
            romania.setCode("RO");
            romania.setContinentId(europe.getId());
            ctryDao.create(romania);
            System.out.println("Created Country: id=" + romania.getId());

            // 3) Lookup by id
            Continent foundCont = contDao.findById(europe.getId());
            System.out.println("Found Continent by ID: " + foundCont.getName());

            Country foundCtry = ctryDao.findByName("Romania");
            System.out.println("Found Country by Name: " + foundCtry.getName()
                               + ", Code=" + foundCtry.getCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}