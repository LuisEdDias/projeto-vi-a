package lat.luisdias.doacao_api.repositories;

import lat.luisdias.doacao_api.entities.Doacao;
import org.springframework.data.jpa.repository.JpaRepository;

// Declaração da interface JPA para acesso ao banco de dados
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {
}
