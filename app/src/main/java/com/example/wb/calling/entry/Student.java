package com.example.wb.calling.entry;

/**
 * Created by wb on 16/2/23.
 */
public class Student implements Comparable<Student>{

    String name;
    String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int compareTo(Student another) {
        return this.number.compareTo(another.getNumber());
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
