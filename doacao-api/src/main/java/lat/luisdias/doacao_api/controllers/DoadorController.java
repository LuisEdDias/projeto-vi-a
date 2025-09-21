package lat.luisdias.doacao_api.controllers;

import jakarta.validation.Valid;
import lat.luisdias.doacao_api.dtos.doador.DoadorCreateDTO;
import lat.luisdias.doacao_api.dtos.doador.DoadorGetDTO;
import lat.luisdias.doacao_api.dtos.doador.DoadorUpdateDTO;
import lat.luisdias.doacao_api.dtos.doador.DoadorWithDoacoesGetDTO;
import lat.luisdias.doacao_api.services.DoadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    // Retorna a lista de doadores cadastrados
    @GetMapping
    public List<DoadorGetDTO> list() {
        return doadorService.findAll().stream().map(DoadorGetDTO::new).toList();
    }

    // Retorna um doador específico com a lista de doações
    @GetMapping("/{id}")
    public DoadorWithDoacoesGetDTO findById(@PathVariable Long id) {
        return new DoadorWithDoacoesGetDTO(doadorService.getByIdOrThrowWithDoacoes(id));
    }

    // Cadastra um novo doador
    @PostMapping
    public ResponseEntity<DoadorGetDTO> create(
            @RequestBody @Valid DoadorCreateDTO doadorCreateDTO,
            UriComponentsBuilder uriBuilder
    ) {
        DoadorGetDTO doador = new DoadorGetDTO(doadorService.create(doadorCreateDTO));
        URI uri = uriBuilder.path("/doador/{id}").buildAndExpand(doador.id()).toUri();
        return ResponseEntity.created(uri).body(doador);
    }

    // Recebe a requisição para atualizar os dados de um doador
    @PutMapping("/{id}")
    public DoadorGetDTO update(
            @PathVariable Long id,
            @RequestBody @Valid DoadorUpdateDTO doadorUpdateDTO
    ) {
        return new DoadorGetDTO(doadorService.update(id, doadorUpdateDTO));
    }

    // Recebe a requisição para deletar o cadastro de um doador
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        doadorService.delete(id);
    }
}
