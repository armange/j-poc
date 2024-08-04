package br.com.armange;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configs")
public class Endpoint {

    @Value("${example.config-two:two-default}")
    private String configTwo;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/config-two", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getConfigTwo() {
        return configTwo;
    }
}
