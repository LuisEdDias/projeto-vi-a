package lat.luisdias.doacao_api.controllers;

import lat.luisdias.doacao_api.services.DoadorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
* Responsável por receber as requisições relacionadas a doadores via HTTP
* e devolver uma resposta adequada
*/
@RestController
@RequestMapping("doador")
public class DoadorController {
    // Declaração da dependência da classe de serviço
    private final DoadorService doadorService;

    // Injeção de dependência via construtor
    public DoadorController(DoadorService doadorService) {
        this.doadorService = doadorService;
    }

}
