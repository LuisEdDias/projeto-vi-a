package lat.luisdias.doacao_api.repositories;

import lat.luisdias.doacao_api.entities.Doacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// Declaração da interface JPA para acesso ao banco de dados
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {
    @Query("SELECT d FROM doacao d LEFT JOIN d.itens WHERE d.id = :id")
    Optional<Doacao> findByIdWithItems(@Param("id") Long id);
}
