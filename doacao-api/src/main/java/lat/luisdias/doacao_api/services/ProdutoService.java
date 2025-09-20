package lat.luisdias.doacao_api.services;

import jakarta.persistence.EntityNotFoundException;
import lat.luisdias.doacao_api.dtos.ProdutoCreateDTO;
import lat.luisdias.doacao_api.dtos.ProdutoUpdateDTO;
import lat.luisdias.doacao_api.entities.Produto;
import lat.luisdias.doacao_api.repositories.DoacaoProdutoRepository;
import lat.luisdias.doacao_api.repositories.ProdutoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Classe responsável pelas regras de negócio de Produto
@Service
public class ProdutoService {
    // Declaração da dependência do repositório de Produto
    private final ProdutoRepository produtoRepository;

    // Declaração da dependência do repositório de DoacaoProduto
    private final DoacaoProdutoRepository doacaoProdutoRepository;

    // Injeção de dependências via construtor
    public ProdutoService(
            ProdutoRepository produtoRepository,
            DoacaoProdutoRepository doacaoProdutoRepository
    ) {
        this.produtoRepository = produtoRepository;
        this.doacaoProdutoRepository = doacaoProdutoRepository;
    }

    // Retorna todos os produtos do banco de dados
    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    // Retorna um produto caso o ID informado exista
    public Produto getByIdOrThrow(Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);

        return produto.orElseThrow(
                () -> new EntityNotFoundException("Produto não encontrado")
        );
    }

    // Cria um novo produto no banco de dados
    public Produto create(ProdutoCreateDTO produtoCreateDTO) {
        return produtoRepository.save(new Produto(produtoCreateDTO));
    }

    // Atualiza os dados de um produto caso ele exista
    public Produto update(Long id, ProdutoUpdateDTO produtoUpdateDTO) {
        Produto produto = getByIdOrThrow(id);
        produto.update(produtoUpdateDTO);
        return produtoRepository.save(produto);
    }

    // Deleta um produto caso ele exista
    public void delete(Long id) {
        Produto produto = getByIdOrThrow(id);
        if (doacaoProdutoRepository.existsByProduto(produto)) {
            throw new DataIntegrityViolationException(
                    "Esse produto não pode ser deletado pois está associado a uma ou mais doações"
            );
        }
        produtoRepository.delete(produto);
    }
}
