package ru.fcpsr.informationtable.controllers;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.fcpsr.informationtable.component.QrCodeGenerator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final QrCodeGenerator qrCodeGenerator;

    @GetMapping("/")
    public Mono<Rendering> homePage(ServerWebExchange serverWebExchange){
        try {
            String scheme = serverWebExchange.getRequest().getURI().getScheme();
            String serverName = serverWebExchange.getRequest().getHeaders().getHost().getHostName();
            int serverPort = serverWebExchange.getRequest().getURI().getPort();
            URL currentUrl = new URL(scheme, serverName, serverPort, "");
            String host = currentUrl.getHost() + ":8085";
            return Mono.just(
                    Rendering.view("template")
                            .modelAttribute("title","XI Международный конгресс БЕЗОПАСНЫЙ СПОРТ-2024")
                            .modelAttribute("index","bare-code-page")
                            .modelAttribute("qrCodeDayOne", qrCodeGenerator.generateQrCode(host + "/day/one"))
                            .modelAttribute("qrCodeDayTwo", qrCodeGenerator.generateQrCode(host + "/day/two"))
                            .build()
            );
        } catch (IOException | WriterException e) {
            return Mono.just(Rendering.redirectTo("/day/one").build());
        }
    }
}
