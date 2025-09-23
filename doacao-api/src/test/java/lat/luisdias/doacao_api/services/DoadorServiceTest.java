package lat.luisdias.doacao_api.services;

import jakarta.persistence.EntityNotFoundException;
import lat.luisdias.doacao_api.dtos.doador.DoadorCreateDTO;
import lat.luisdias.doacao_api.dtos.doador.DoadorUpdateDTO;
import lat.luisdias.doacao_api.entities.Doacao;
import lat.luisdias.doacao_api.entities.Doador;
import lat.luisdias.doacao_api.repositories.DoadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoadorServiceTest {
    @Mock
    private DoadorRepository doadorRepository;

    @Mock
    private Doacao doacao;

    @InjectMocks
    private DoadorService doadorService;

    private Doador doador;
    private DoadorCreateDTO createDTO;
    private DoadorUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        createDTO = new DoadorCreateDTO();
        createDTO.setNome("Luís");
        createDTO.setEmail("test@mail.com");
        createDTO.setDocIdentificacao("12345678901");

        updateDTO = new DoadorUpdateDTO("Eduardo", "email@example.com");

        doador = spy(new Doador(createDTO));
    }

    @Test
    void findAll_ShouldReturnListOfDoadores() {
        when(doadorRepository.findAll()).thenReturn(List.of(doador));

        List<Doador> result = doadorService.findAll();

        assertThat(result).hasSize(1).contains(doador);
        verify(doadorRepository).findAll();
    }

    @Test
    void getByIdOrThrow_ShouldReturnDoador_WhenExists() {
        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doador));

        Doador result = doadorService.getByIdOrThrow(1L);

        assertThat(result).isEqualTo(doador);
        verify(doadorRepository).findById(1L);
    }

    @Test
    void getByIdOrThrow_ShouldThrow_WhenNotExists() {
        when(doadorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> doadorService.getByIdOrThrow(1L));
    }

    @Test
    void getByIdOrThrowWithDoacoes_ShouldReturnDoador_WhenExists() {
        when(doadorRepository.findByIdWithDoacoes(1L)).thenReturn(Optional.of(doador));

        Doador result = doadorService.getByIdOrThrowWithDoacoes(1L);

        assertThat(result).isEqualTo(doador);
        verify(doadorRepository).findByIdWithDoacoes(1L);
    }

    @Test
    void getByIdOrThrowWithDoacoes_ShouldThrow_WhenNotExists() {
        when(doadorRepository.findByIdWithDoacoes(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> doadorService.getByIdOrThrowWithDoacoes(1L));
    }

    @Test
    void create_ShouldSaveDoador_WhenDocIdentificacaoIsUnique() {
        when(doadorRepository.existsByDocIdentificacao(createDTO.getDocIdentificacao())).thenReturn(false);
        when(doadorRepository.save(any(Doador.class))).thenReturn(doador);

        Doador result = doadorService.create(createDTO);

        assertThat(result).isEqualTo(doador);
        verify(doadorRepository).save(any(Doador.class));
    }

    @Test
    void create_ShouldThrow_WhenDocIdentificacaoAlreadyExists() {
        when(doadorRepository.existsByDocIdentificacao(createDTO.getDocIdentificacao())).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> doadorService.create(createDTO));
        verify(doadorRepository, never()).save(any(Doador.class));
    }

    @Test
    void update_ShouldSaveUpdatedDoador_WhenExists() {
        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doador));
        when(doadorRepository.save(doador)).thenReturn(doador);

        Doador result = doadorService.update(1L, updateDTO);

        assertThat(result).isEqualTo(doador);
        assertThat(result.getNome()).isEqualTo(updateDTO.nome());
        assertThat(result.getEmail()).isEqualTo(updateDTO.email());
        verify(doadorRepository).save(doador);
    }

    @Test
    void update_ShouldThrow_WhenNotExists() {
        when(doadorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> doadorService.update(1L, updateDTO));
    }

    @Test
    void delete_ShouldRemoveDoador_WhenNoDoacoes() {
        when(doador.getDoacoes()).thenReturn(List.of());
        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doador));

        doadorService.delete(1L);

        verify(doadorRepository).delete(doador);
    }

    @Test
    void delete_ShouldThrow_WhenHasDoacoes() {
        when(doador.getDoacoes()).thenReturn((List.of(doacao)));
        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doador));

        assertThrows(DataIntegrityViolationException.class, () -> doadorService.delete(1L));
        verify(doadorRepository, never()).delete(doador);
    }
}
