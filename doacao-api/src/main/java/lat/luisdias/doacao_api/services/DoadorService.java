package lat.luisdias.doacao_api.services;

import jakarta.persistence.EntityNotFoundException;
import lat.luisdias.doacao_api.dtos.doador.DoadorCreateDTO;
import lat.luisdias.doacao_api.dtos.doador.DoadorUpdateDTO;
import lat.luisdias.doacao_api.entities.Doador;
import lat.luisdias.doacao_api.repositories.DoadorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Classe responsável pelas regras de negócio de Doador
@Service
public class DoadorService {
    // Declaração da dependência do repositório
    private final DoadorRepository doadorRepository;

    // Injeção de dependência via construtor
    public DoadorService (DoadorRepository doadorRepository) {
        this.doadorRepository = doadorRepository;
    }

    // Retorna a lista de doadores cadastrados
    public List<Doador> findAll(){
        return doadorRepository.findAll();
    }

    // Retorna um doador caso exista
    public Doador getByIdOrThrow(Long id){
        return doadorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Doador não encontrado")
        );
    }

    // Cria um novo doador se os dados forem válidos
    @Transactional
    public Doador create(DoadorCreateDTO doadorCreateDTO){
        if (doadorRepository.existsByDocIdentificacao(doadorCreateDTO.getDocIdentificacao())) {
            throw new DataIntegrityViolationException(
                    "Já existe um dadastro com este documento de identificação"
            );
        }
        return doadorRepository.save(new Doador(doadorCreateDTO));
    }

    // Atualiza os dados de um doador caso exista
    @Transactional
    public Doador update(Long id, DoadorUpdateDTO doadorUpdateDTO) {
        Doador doador = getByIdOrThrow(id);
        doador.update(doadorUpdateDTO);
        return doadorRepository.save(doador);
    }

    // Deleta um doador do banco de dados caso exista
    @Transactional
    public void delete(Long id){
        Doador doador = getByIdOrThrow(id);
        // Verifica se existem doações associadas ao doador
        if (!doador.getDoacoes().isEmpty()){
            throw new DataIntegrityViolationException(
                    "Não é possível remover o doador pois existem uma ou mais doações associadas a ele"
            );
        }
        doadorRepository.delete(doador);
    }
}
