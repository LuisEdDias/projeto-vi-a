package lat.luisdias.doacao_api.services;

import jakarta.persistence.EntityNotFoundException;
import lat.luisdias.doacao_api.dtos.produto.ProdutoCreateDTO;
import lat.luisdias.doacao_api.dtos.produto.ProdutoUpdateDTO;
import lat.luisdias.doacao_api.entities.Produto;
import lat.luisdias.doacao_api.entities.ProdutoCategoria;
import lat.luisdias.doacao_api.entities.UnidadeControle;
import lat.luisdias.doacao_api.repositories.DoacaoProdutoRepository;
import lat.luisdias.doacao_api.repositories.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {
    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private DoacaoProdutoRepository doacaoProdutoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;
    private ProdutoCreateDTO createDTO;
    private ProdutoUpdateDTO updateDTO;

    @BeforeEach
    void setup() {
        createDTO = new ProdutoCreateDTO("Arroz", ProdutoCategoria.ALIMENTO, UnidadeControle.KG);
        updateDTO = new ProdutoUpdateDTO("Leite", null, UnidadeControle.LITROS);
        produto = new Produto(createDTO);
    }

    @Test
    void findAll_ShouldReturnListOfProdutos() {
        when(produtoRepository.findAll()).thenReturn(List.of(produto));

        List<Produto> result = produtoService.findAll();

        assertEquals(1, result.size());
        assertEquals("Arroz", result.get(0).getNome());
        verify(produtoRepository).findAll();
    }

    @Test
    void getByIdOrThrow_WhenProdutoExists_ShouldReturnProduto() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Produto result = produtoService.getByIdOrThrow(1L);

        assertEquals("Arroz", result.getNome());
        verify(produtoRepository).findById(1L);
    }

    @Test
    void getByIdOrThrow_WhenProdutoNotFound_ShouldThrowException() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> produtoService.getByIdOrThrow(99L));
    }

    @Test
    void create_ShouldSaveAndReturnProduto() {
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto result = produtoService.create(createDTO);

        assertNotNull(result);
        assertEquals("Arroz", result.getNome());
        verify(produtoRepository).save(any(Produto.class));
    }

    @Test
    void update_WhenProdutoExists_ShouldUpdateAndReturnProduto() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto result = produtoService.update(1L, updateDTO);

        assertEquals("Leite", result.getNome());
        assertEquals("ALIMENTO", result.getCategoria().name());
        assertEquals("LITROS", result.getUnidadeControle().name());
        verify(produtoRepository).save(produto);
    }

    @Test
    void update_WhenProdutoNotFound_ShouldThrowException() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> produtoService.update(99L, updateDTO));
    }

    @Test
    void delete_WhenProdutoNotAssociated_ShouldDeleteProduto() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(doacaoProdutoRepository.existsByProduto(produto)).thenReturn(false);

        produtoService.delete(1L);

        verify(produtoRepository).delete(produto);
    }

    @Test
    void delete_WhenProdutoAssociated_ShouldThrowException() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(doacaoProdutoRepository.existsByProduto(produto)).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class,
                () -> produtoService.delete(1L));
    }
}
