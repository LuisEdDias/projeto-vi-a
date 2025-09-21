package lat.luisdias.doacao_api.repositories;

import lat.luisdias.doacao_api.entities.Doador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// Declaração da interface JPA para acesso ao banco de dados
public interface DoadorRepository extends JpaRepository<Doador, Long> {
    boolean existsByDocIdentificacao(String docIdentificacao);

    @Query("SELECT d FROM doador d LEFT JOIN d.doacoes WHERE d.id = :id")
    Optional<Doador> findByIdWithDoacoes(@Param("id") Long id);
}
