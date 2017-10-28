package com.ge.bean;

import java.util.List;

public class SchoolClass {

  public int id;
  public String name;
  public List<Students> students;

  public List<Students> getStudents() {
    return students;
  }

  public void setStudents(List<Students> students) {
    this.students = students;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
