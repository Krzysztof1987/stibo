package com.stibo.demo.report.controller;

import com.stibo.demo.report.model.Datastandard;
import com.stibo.demo.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.websocket.DeploymentException;
import java.net.URI;

import static java.util.stream.Collectors.joining;


@RestController
public class ReportController {

    private final RestTemplate restTemplate;
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService, RestTemplateBuilder restTemplateBuilder) {
        this.reportService = reportService;
        this.restTemplate = restTemplateBuilder.build();
    }

    @RequestMapping(value = "/report/{datastandardId}/{categoryId}")
    public String report(@PathVariable String datastandardId, @PathVariable String categoryId, @RequestHeader String authorization) throws DeploymentException {
        URI uri = URI.create("https://tagglo-dev.io/api/datastandards/v1/datastandards/" + datastandardId);
        RequestEntity<Void> request = RequestEntity.get(uri).header("Authorization", authorization).build();
        Datastandard datastandard = restTemplate.exchange(request, Datastandard.class).getBody();


            return   reportService.report(datastandard, categoryId)
                    // naive CSV conversion, assuming cells do not have quotes
                    .map(row -> row.collect(joining("\",\"", "\"", "\""))).collect(joining("\n"));
    }
}
