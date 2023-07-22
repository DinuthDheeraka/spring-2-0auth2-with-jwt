/**
 * @author :  Dinuth Dheeraka
 * Created : 7/22/2023 12:43 PM
 */
package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pro")
public class Controller {

    @GetMapping
    public String product(){
        return "1234";
    }
}
