package lat.luisdias.doacao_api.controllers;

import jakarta.validation.Valid;
import lat.luisdias.doacao_api.dtos.produto.ProdutoCreateDTO;
import lat.luisdias.doacao_api.dtos.produto.ProdutoGetDTO;
import lat.luisdias.doacao_api.dtos.produto.ProdutoUpdateDTO;
import lat.luisdias.doacao_api.services.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

// Responsável por receber as requisições do usuário via HTTP e devolver uma resposta adequada
@RestController
@RequestMapping("produto")
public class ProdutoController {
    // Declaração da dependência da classe de serviço
    private final ProdutoService produtoService;

    // Injeção de dependência via construtor
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // Retorna todos os produtos cadastrados
    @GetMapping
    public List<ProdutoGetDTO> list() {
        return produtoService.findAll()
                .stream()
                .map(ProdutoGetDTO::new)
                .toList();
    }

    // Recebe a requisição com os dados para criar um novo produto
    @PostMapping
    public ResponseEntity<ProdutoGetDTO> save(
            @RequestBody @Valid ProdutoCreateDTO produtoDTO,
            UriComponentsBuilder uriBuilder
    ) {
        ProdutoGetDTO produto = new ProdutoGetDTO(produtoService.create(produtoDTO));
        URI uri = uriBuilder.path("/produto/{id}").buildAndExpand(produto.id()).toUri();
        return ResponseEntity.created(uri).body(produto);
    }

    // Recebe a requisição para atualizar um produto
    @PutMapping("/{id}")
    public ProdutoGetDTO update(
            @PathVariable Long id,
            @RequestBody @Valid ProdutoUpdateDTO produtoDTO
    ) {
        return new ProdutoGetDTO(produtoService.update(id, produtoDTO));
    }

    // Recebe a requisição para deletar um produto
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        produtoService.delete(id);
    }
}
