package com.morpheus.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;

import com.morpheus.demo.entitys.NewsPortal;

@CrossOrigin(origins = "*")
public class ControllerNewsPortal {
    List<NewsPortal> portals;

}
