package com.gameguildstudios.markyourself;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Assignment implements Comparable<Assignment> {

    public String name;
    public double weight;
    public double grade;
    public String date;
    public String target;

    public Assignment(String name, double weight, double grade, String date, String target) {
        this.name = name;
        this.weight = weight;
        this.grade = grade;
        this.date = date;
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getGrade() {
        return grade;
    }

    public String getDate() {
        return date;
    }

    public String getTarget(){ return target;}

    @Override
    public int compareTo(Assignment o) {
        if (getDate() == null || o.getDate() == null)
            return 0;
        else {
            return getDate().compareTo(o.getDate());
        }
    }
}