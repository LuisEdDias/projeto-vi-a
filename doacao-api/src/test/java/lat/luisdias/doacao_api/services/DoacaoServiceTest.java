package lat.luisdias.doacao_api.services;

import jakarta.persistence.EntityNotFoundException;
import lat.luisdias.doacao_api.dtos.doacao.DoacaoCreateDTO;
import lat.luisdias.doacao_api.dtos.doacao.ProdutoQuantidadeDTO;
import lat.luisdias.doacao_api.dtos.doador.DoadorCreateDTO;
import lat.luisdias.doacao_api.dtos.produto.ProdutoCreateDTO;
import lat.luisdias.doacao_api.entities.*;
import lat.luisdias.doacao_api.repositories.DoacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoacaoServiceTest {
    @Mock
    private DoacaoRepository doacaoRepository;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private DoadorService doadorService;

    @InjectMocks
    private DoacaoService doacaoService;

    private Doador doador;
    private Produto produto;
    private Doacao doacao;

    @BeforeEach
    void setUp() {
        var doadorDTO = new DoadorCreateDTO();
        doadorDTO.setNome("Luís");
        doadorDTO.setEmail("test@mail.com");
        doadorDTO.setDocIdentificacao("12345678901");
        doador = new Doador(doadorDTO);

        var produtoDTO = new ProdutoCreateDTO("Arroz", ProdutoCategoria.ALIMENTO, UnidadeControle.KG);
        produto = spy(new Produto(produtoDTO));

        doacao = new Doacao("Doação teste", LocalDate.now(), doador, new ArrayList<>());
    }

    @Test
    void findAll_ShouldReturnListOfDoacoes() {
        when(doacaoRepository.findAll()).thenReturn(List.of(doacao));

        List<Doacao> result = doacaoService.findAll();

        assertThat(result).hasSize(1).contains(doacao);
        verify(doacaoRepository).findAll();
    }

    @Test
    void findByIdWithItemsOrThrow_ShouldReturnDoacao_WhenExists() {
        when(doacaoRepository.findByIdWithItems(1L)).thenReturn(Optional.of(doacao));

        Doacao result = doacaoService.findByIdWithItemsOrThrow(1L);

        assertThat(result).isEqualTo(doacao);
    }

    @Test
    void findByIdWithItemsOrThrow_ShouldThrow_WhenNotFound() {
        when(doacaoRepository.findByIdWithItems(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> doacaoService.findByIdWithItemsOrThrow(1L));
    }

    @Test
    void create_ShouldSaveDoacaoWithItems() {
        ProdutoQuantidadeDTO item1DTO = new ProdutoQuantidadeDTO(1L, BigDecimal.valueOf(2));
        ProdutoQuantidadeDTO item2DTO = new ProdutoQuantidadeDTO(1L, BigDecimal.valueOf(2));
        DoacaoCreateDTO doacaoCreateDTO = new DoacaoCreateDTO(
                2L,
                "Nova Doação",
                LocalDate.now(),
                List.of(item1DTO, item2DTO)
        );

        when(doadorService.getByIdOrThrow(2L)).thenReturn(doador);
        when(produtoService.getByIdOrThrow(1L)).thenReturn(produto);
        when(doacaoRepository.save(any(Doacao.class))).thenAnswer(inv -> inv.getArgument(0));

        Doacao result = doacaoService.create(doacaoCreateDTO);

        assertThat(result.getDescricao()).isEqualTo("Nova Doação");
        assertThat(result.getItens()).hasSize(1);
        assertThat(result.getItens().get(0).getProduto()).isEqualTo(produto);
        assertThat(result.getItens().get(0).getQuantidade()).isEqualTo(BigDecimal.valueOf(4));

        verify(doacaoRepository).save(any(Doacao.class));
    }

    @Test
    void create_ShouldThrow_WhenNoItemsProvided() {
        DoacaoCreateDTO doacaoCreateDTO = new DoacaoCreateDTO(
                1L,
                "Doação vazia",
                LocalDate.now(),
                List.of()
        );

        when(doadorService.getByIdOrThrow(1L)).thenReturn(doador);

        assertThrows(IllegalArgumentException.class, () -> doacaoService.create(doacaoCreateDTO));
    }

    @Test
    void addItem_ShouldAddNewItem_WhenNotExists() {
        when(doacaoRepository.findByIdWithItems(1L)).thenReturn(Optional.of(doacao));
        when(produtoService.getByIdOrThrow(2L)).thenReturn(produto);
        when(doacaoRepository.save(any(Doacao.class))).thenAnswer(inv -> inv.getArgument(0));

        Doacao result = doacaoService.addItem(
                1L,
                new ProdutoQuantidadeDTO(2L, BigDecimal.valueOf(1))
        );

        assertThat(result.getItens()).hasSize(1);
        assertThat(result.getItens().get(0).getProduto()).isEqualTo(produto);
    }

    @Test
    void addItem_ShouldIncreaseQuantity_WhenItemAlreadyExists() {
        DoacaoProduto doacaoProduto = new DoacaoProduto(BigDecimal.valueOf(1), produto, doacao);
        doacao.getItens().add(doacaoProduto);

        when(produto.getId()).thenReturn(2L);
        when(doacaoRepository.findByIdWithItems(1L)).thenReturn(Optional.of(doacao));
        when(produtoService.getByIdOrThrow(2L)).thenReturn(produto);
        when(doacaoRepository.save(any(Doacao.class))).thenAnswer(inv -> inv.getArgument(0));

        Doacao result = doacaoService.addItem(
                1L,
                new ProdutoQuantidadeDTO(2L, BigDecimal.valueOf(10))
        );

        assertThat(result.getItens()).hasSize(1);
        assertThat(result.getItens().get(0).getQuantidade()).isEqualTo(BigDecimal.valueOf(11));
    }

    @Test
    void removeItem_ShouldRemoveItem_WhenExists() {
        DoacaoProduto item = spy(new DoacaoProduto(BigDecimal.ONE, produto, doacao));

        doacao.getItens().add(item);

        when(item.getId()).thenReturn(2L);
        when(doacaoRepository.findByIdWithItems(1L)).thenReturn(Optional.of(doacao));
        when(doacaoRepository.save(any(Doacao.class))).thenAnswer(inv -> inv.getArgument(0));

        Doacao result = doacaoService.removeItem(1L, 2L);

        assertThat(result.getItens()).isEmpty();
    }

    @Test
    void delete_ShouldRemoveDoacao_WhenExists() {
        when(doacaoRepository.findByIdWithItems(1L)).thenReturn(Optional.of(doacao));

        doacaoService.delete(1L);

        verify(doacaoRepository).delete(doacao);
    }

    @Test
    void delete_ShouldThrow_WhenNotFound() {
        when(doacaoRepository.findByIdWithItems(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> doacaoService.delete(1L));
    }
}
