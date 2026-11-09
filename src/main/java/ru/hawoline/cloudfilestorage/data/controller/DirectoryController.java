package ru.hawoline.cloudfilestorage.data.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("directory")
public class DirectoryController {

    @GetMapping
    public void getDirectoryInfo(@RequestParam("path") String path) {

    }
}
