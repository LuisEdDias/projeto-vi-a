package lat.luisdias.doacao_api.controllers;

import jakarta.validation.Valid;
import lat.luisdias.doacao_api.dtos.doacao.DoacaoCreateDTO;
import lat.luisdias.doacao_api.dtos.doacao.DoacaoGetDTO;
import lat.luisdias.doacao_api.dtos.doacao.DoacaoWithItemsGetDTO;
import lat.luisdias.doacao_api.dtos.doacao.ProdutoQuantidadeDTO;
import lat.luisdias.doacao_api.services.DoacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/*
 * Responsável por receber as requisições relacionadas a doações via HTTP
 * e devolver uma resposta adequada
 */
@RestController
@RequestMapping("doacao")
public class DoacaoController {
    // Declaração da dependência de Doacao
    private final DoacaoService doacaoService;

    // Injeção de dependência via construtor
    public DoacaoController(DoacaoService doacaoService) {
        this.doacaoService = doacaoService;
    }

    // Retorna as doações registradas no sistema
    @GetMapping
    public List<DoacaoGetDTO> list() {
        return doacaoService.findAll().stream().map(DoacaoGetDTO::new).toList();
    }

    // Retorna uma doação específica com dados completos
    @GetMapping("/{id}")
    public DoacaoWithItemsGetDTO findById(@PathVariable Long id) {
        return new DoacaoWithItemsGetDTO(doacaoService.findByIdWithItemsOrThrow(id));
    }

    // Recebe a requisição para registrar uma nova doação
    @PostMapping
    public ResponseEntity<DoacaoWithItemsGetDTO> save(
            UriComponentsBuilder uriBuilder,
            @RequestBody @Valid DoacaoCreateDTO doacaoCreateDTO
    ) {
        DoacaoWithItemsGetDTO doacao = new DoacaoWithItemsGetDTO(
                doacaoService.create(doacaoCreateDTO)
        );
        URI uri = uriBuilder.path("/doacao/{id}").buildAndExpand(doacao.doacaoId()).toUri();
        return ResponseEntity.created(uri).body(doacao);
    }

    // Recebe a requisição para adicionar um item a uma doação existente
    @PutMapping("/{doacaoId}/addItem")
    public DoacaoWithItemsGetDTO addItem(
            @PathVariable Long doacaoId,
            @RequestBody @Valid ProdutoQuantidadeDTO produtoQuantidadeDTO
    ) {
        return new DoacaoWithItemsGetDTO(doacaoService.addItem(doacaoId, produtoQuantidadeDTO));
    }

    // Recebe a requisição para remover um item de uma doação existente
    @PutMapping("/{doacaoId}/removeItem/{itemId}")
    public DoacaoWithItemsGetDTO removeItem(
            @PathVariable Long doacaoId,
            @PathVariable Long itemId
    ) {
        return new DoacaoWithItemsGetDTO(doacaoService.removeItem(doacaoId, itemId));
    }

    // Lida com a remoção de uma doação
    @DeleteMapping("/{doacaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long doacaoId) {
        doacaoService.delete(doacaoId);
    }
}
