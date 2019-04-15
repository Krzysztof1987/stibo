package com.stibo.demo.report.model;

import java.util.ArrayList;
import java.util.List;

public class Attribute   {
  private String id;
  private String name;
  private String description;
  private AttributeType type;
  private List<AttributeLink> attributeLinks;
  private List<String> groupIds;

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AttributeType getType() {
    return type;
  }

  public void setType(AttributeType type) {
    this.type = type;
  }

  public List<AttributeLink> getAttributeLinks() {
    return attributeLinks;
  }

  public void setAttributeLinks(List<AttributeLink> attributeLinks) {
    this.attributeLinks = attributeLinks;
  }

  public List<String> getGroupIds() {
    return groupIds;
  }

  public void setGroupIds(List<String> groupIds) {
    this.groupIds = groupIds;
  }
}

