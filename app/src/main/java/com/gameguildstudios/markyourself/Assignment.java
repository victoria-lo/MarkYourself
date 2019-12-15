package com.gameguildstudios.markyourself;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Assignment implements Comparable<Assignment> {

    public String name;
    public Integer weight;
    public Integer grade;
    public String date;
    public String target;

    public Assignment(String name, Integer weight, Integer grade, String date, String target) {
        this.name = name;
        this.weight = weight;
        this.grade = grade;
        this.date = date;
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getGrade() {
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