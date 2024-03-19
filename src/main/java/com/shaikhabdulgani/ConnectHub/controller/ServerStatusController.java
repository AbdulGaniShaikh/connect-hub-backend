package com.shaikhabdulgani.ConnectHub.controller;

import com.shaikhabdulgani.ConnectHub.util.enums.ServerStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/server-status")
public class ServerStatusController {

    @GetMapping
    public ServerStatus getStatus(){
        return ServerStatus.RUNNING;
    }

}
