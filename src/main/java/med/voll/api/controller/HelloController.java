package med.voll.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //Estamos trabalhando com uma API REST, logo para declarar que essa classe é um controller utilizamos RestController
@RequestMapping("/hello") //Aqui é definida qual URL este controller vai responder, isto é: se chegar uma requisição localhost:8080/hello, cairá nesse controller
public class HelloController {

    @GetMapping  //Esse método será chamado quando houver uma requisição "/hello", devido ao GetMapping
    public String olaMundo(){
        return "Hello world Spring!";
    }


}
