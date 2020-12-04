package ru.skillbox.monolithicapp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SimpleErrorController implements ErrorController {

    @RequestMapping("error")
    private String handleError() {
        return "404.html";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
