package lat.luisdias.doacao_api.repositories;

import lat.luisdias.doacao_api.entities.DoacaoProduto;
import lat.luisdias.doacao_api.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

// Declaração da interface JPA para acesso ao banco de dados
public interface DoacaoProdutoRepository extends JpaRepository<DoacaoProduto, Long> {
    boolean existsByProduto(Produto produto);
}
