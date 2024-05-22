package ru.fcpsr.informationtable.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/day")
public class DaysController {
    @GetMapping("/one")
    public Mono<Rendering> dayOne(){
        return Mono.just(
                Rendering.view("template")
                        .modelAttribute("title","XI Международный конгресс БЕЗОПАСНЫЙ СПОРТ-2024")
                        .modelAttribute("index","day-one-page")
                        .build()
        );
    }

    @GetMapping("/two")
    public Mono<Rendering> dayTwo(){
        return Mono.just(
                Rendering.view("template")
                        .modelAttribute("title","XI Международный конгресс БЕЗОПАСНЫЙ СПОРТ-2024")
                        .modelAttribute("index","day-two-page")
                        .build()
        );
    }
}
