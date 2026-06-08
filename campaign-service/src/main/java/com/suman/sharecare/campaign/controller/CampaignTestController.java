package com.suman.sharecare.campaign.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/campaign")
public class CampaignTestController {
    @GetMapping("/test")
    public String test() {
        return "Hello from CampaignTestController through api gateway";
    }
}
