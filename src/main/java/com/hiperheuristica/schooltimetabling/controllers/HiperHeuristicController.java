package com.hiperheuristica.schooltimetabling.controllers;

import com.hiperheuristica.schooltimetabling.services.HiperHeuristicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/start")
@RequiredArgsConstructor
public class HiperHeuristicController {

    @Autowired
    private HiperHeuristicService service;

    @PostMapping
    public void start() {
        service.start();
    }
}
