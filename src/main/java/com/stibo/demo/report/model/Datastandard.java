package com.stibo.demo.report.model;

import java.util.List;

public class Datastandard {
  private String id;
  private String name;
  private List<Category> categories;
  private List<Attribute> attributes;
  private List<AttributeGroup> attributeGroups;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Category> getCategories() {
    return categories;
  }

  public void setCategories(List<Category> categories) {
    this.categories = categories;
  }

  public List<Attribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<Attribute> attributes) {
    this.attributes = attributes;
  }

  public List<AttributeGroup> getAttributeGroups() {
    return attributeGroups;
  }

  public void setAttributeGroups(List<AttributeGroup> attributeGroups) {
    this.attributeGroups = attributeGroups;
  }
}

