package br.com.yugitilidades.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@Log4j2
@RestController
public class PingController {
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        log.debug("Request received for /ping endpoint");
        return new ResponseEntity<String>("Pong ヽ(・∀・)ﾉ", HttpStatus.OK);
    }
}
