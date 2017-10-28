package com.dbpersis.utils;

public class Association {

  public String ownerClass;
  public String ownerProperty;
  public String ownerParam;

  public String targetClass;
  public String targetProperty;

  public String getOwnerClass() {
    return ownerClass;
  }

  public void setOwnerClass(String ownerClass) {
    this.ownerClass = ownerClass;
  }

  public String getOwnerProperty() {
    return ownerProperty;
  }

  public void setOwnerProperty(String ownerProperty) {
    this.ownerProperty = ownerProperty;
  }

  public String getOwnerParam() {
    return ownerParam;
  }

  public void setOwnerParam(String ownerParam) {
    this.ownerParam = ownerParam;
  }

  public String getTargetClass() {
    return targetClass;
  }

  public void setTargetClass(String targetClass) {
    this.targetClass = targetClass;
  }

  public String getTargetProperty() {
    return targetProperty;
  }

  public void setTargetProperty(String targetProperty) {
    this.targetProperty = targetProperty;
  }
}
