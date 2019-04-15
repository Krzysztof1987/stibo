package com.stibo.demo.report.service;

import com.stibo.demo.report.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReportService {
    static final int AMOUNT_OF_COLUMNS = 5;
    static final String ASTERIKS = "*";
    static final String TWO_SPACE = "  ";
    static final String BRACKETS_SQUARE = "[]";
    static final String COLON = ":";
    static final String LEFT_BRACKET = "{";
    static final String RIGHT_BRACKET = "}";

    public Stream<Stream<String>> report(Datastandard datastandard, String categoryId) {
        /*
        README: I don't check NullPointers and consistency of data ( don't check that ids from attribute  group or link refer to attribute)
         */
        List<Stream<String>> resultStream = new ArrayList<>();
        Map<String, Category> categoryMap = datastandard.getCategories().stream()
                .filter(category -> category.getId() != null)
                .collect(Collectors.toMap(Category::getId, Function.identity()));
        Map<String, Attribute> attributesMap = datastandard.getAttributes().stream()
                .filter(attr -> attr.getId() != null)
                .collect(Collectors.toMap(Attribute::getId, Function.identity()));
        Map<String, AttributeGroup> attributesGroupMap = datastandard.getAttributeGroups().stream()
                .filter(attributeGroup -> attributeGroup.getId() != null)
                .collect(Collectors.toMap(AttributeGroup::getId, Function.identity()));
        Category cat = categoryMap.get(categoryId);
        List<Category> listCat = new ArrayList<>();
        listCat.add(cat);
        while (cat.getParentId() != null) {
            cat = categoryMap.get(cat.getParentId());
            if (cat.getAttributeLinks() != null && !cat.getAttributeLinks().isEmpty()) {
                listCat.add(cat);
            }
        }

        for (int i = listCat.size() - 1; i >= 0; i--) {
            Category category = listCat.get(i);
            List<String> cells = new ArrayList<>(AMOUNT_OF_COLUMNS);
            for (AttributeLink attributeLink : category.getAttributeLinks()) {
                Attribute attribute = attributesMap.get(attributeLink.getId());
                cells.add(category.getName());

                cells.add(attributeColumName(attributeLink, attribute));
                cells.add(attribute.getDescription());
                cells.add(typeColumn(attributesMap, attribute));
                cells.add(groupColumn(attribute, attributesGroupMap));

                resultStream.add(cells.stream());
                cells = new ArrayList<>(AMOUNT_OF_COLUMNS);
            }
        }

        return resultStream.stream();
    }

    private String typeColumn(Map<String, Attribute> attributesMap, Attribute attribute) {
        StringBuilder typeStringBuilder = new StringBuilder();
        typeStringBuilder.append(attribute.getType().getId());
        if (!attribute.getAttributeLinks().isEmpty()) {
            typeStringBuilder.append(LEFT_BRACKET);
            typeStringBuilder.append("\n");
            for (AttributeLink link : attribute.getAttributeLinks()) {
                Attribute attributeInner = attributesMap.get(link.getId());
                typeStringBuilder.append(TWO_SPACE);
                typeStringBuilder.append(attributeColumName(link, attributeInner));
                typeStringBuilder.append(COLON);
                typeStringBuilder.append(typeColumn(attributeInner));
                typeStringBuilder.append("\n");
            }
            typeStringBuilder.append(RIGHT_BRACKET);
        }
        if (attribute.getType().getMultiValue()) {
            typeStringBuilder.append(BRACKETS_SQUARE);
        }
        return typeStringBuilder.toString();
    }

    private String groupColumn(Attribute attribute, Map<String, AttributeGroup> attributesGroupMap) {
        return attribute.getGroupIds().stream().
                map(group -> attributesGroupMap.get(group).getName())
                .collect(Collectors.joining("\n"));
    }

    private String attributeColumName(AttributeLink link, Attribute attr) {
        return link.getOptional() ? attr.getName() : attr.getName() + ASTERIKS;
    }

    private String typeColumn(Attribute attribute) {
        AttributeType type = attribute.getType();
        return type.getMultiValue() ? type.getId() + BRACKETS_SQUARE : type.getId();
    }
}


