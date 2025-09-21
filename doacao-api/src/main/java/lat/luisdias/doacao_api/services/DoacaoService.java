package lat.luisdias.doacao_api.services;

import jakarta.persistence.EntityNotFoundException;
import lat.luisdias.doacao_api.dtos.doacao.DoacaoCreateDTO;
import lat.luisdias.doacao_api.dtos.doacao.ProdutoQuantidadeDTO;
import lat.luisdias.doacao_api.entities.Doacao;
import lat.luisdias.doacao_api.entities.DoacaoProduto;
import lat.luisdias.doacao_api.entities.Doador;
import lat.luisdias.doacao_api.entities.Produto;
import lat.luisdias.doacao_api.repositories.DoacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// Classe responsável pelas regras de negócio de Doacao
@Service
public class DoacaoService {
    // Declaração da dependência do repositório de Doacao
    private final DoacaoRepository doacaoRepository;

    // Declaração da dependência do serviço de Produto
    private final ProdutoService produtoService;

    //Declaração da dependência do serviço de Doador
    private final DoadorService doadorService;

    // Injeção de dependências via construtor
    public DoacaoService(
            DoacaoRepository doacaoRepository,
            ProdutoService produtoService,
            DoadorService doadorService
    ) {
        this.doacaoRepository = doacaoRepository;
        this.produtoService = produtoService;
        this.doadorService = doadorService;
    }

    // Retorna a lista de doações realizadas
    public List<Doacao> findAll() {
        return doacaoRepository.findAll();
    }

    // Retorna os dados de uma doação caso esxista
    public Doacao findByIdWithItemsOrThrow(Long id) {
        return doacaoRepository.findByIdWithItems(id).orElseThrow(
                () -> new EntityNotFoundException("Doação não encontrada")
        );
    }

    // Cadastra uma nova doação
    @Transactional
    public Doacao create(DoacaoCreateDTO doacaoDTO) {
        Doador doador = doadorService.getByIdOrThrow(doacaoDTO.doadorId());
        Doacao doacao = new Doacao(
                doacaoDTO.descricao(),
                LocalDate.now(),
                doador,
                new ArrayList<>()
        );
        // Valida e converte os itens da lista de doação
        doacaoDTO.itens().stream().collect(
                Collectors.toMap(
                        ProdutoQuantidadeDTO::produtoId,
                        item -> {
                            Produto produto = produtoService.getByIdOrThrow(item.produtoId());
                            return new DoacaoProduto(item.quantidade(), produto, doacao);
                        },
                        (oldValue, newValue) -> {
                            oldValue.addQuantidade(newValue.getQuantidade());
                            return oldValue;
                        }
                )
        ).values().forEach(doacao.getItens()::add);

        if (doacao.getItens().isEmpty()){
            throw new IllegalArgumentException("A doação precisa conter itens válidos");
        }

        return doacaoRepository.save(doacao);
    }

    // Remove um produto da lista de itens da doação
    @Transactional
    public Doacao removeItem(Long idDoacao, Long idDoacaoProduto) {
        Doacao doacao = findByIdWithItemsOrThrow(idDoacao);
        doacao.getItens().removeIf(item -> item.getId().equals(idDoacaoProduto));
        return doacaoRepository.save(doacao);
    }

    // Adiciona um produto na lista de itens da doação
    @Transactional
    public Doacao addItem(Long idDoacao, ProdutoQuantidadeDTO produtoQuantidadeDTO) {
        Doacao doacao = findByIdWithItemsOrThrow(idDoacao);
        Produto produto = produtoService.getByIdOrThrow(produtoQuantidadeDTO.produtoId());

        // Verifica se o produto já existe na lista de itens
        Optional<DoacaoProduto> existente = doacao.getItens().stream()
                .filter(dp -> dp.getProduto().getId().equals(produto.getId()))
                .findFirst();

        if (existente.isPresent()) {
            existente.get().addQuantidade(produtoQuantidadeDTO.quantidade());
        } else {
            doacao.getItens().add(new DoacaoProduto(produtoQuantidadeDTO.quantidade(), produto, doacao));
        }

        return doacaoRepository.save(doacao);
    }

    // Deleta uma doação do banco de dados caso exista
    @Transactional
    public void delete(Long idDoacao) {
        Doacao doacao = findByIdWithItemsOrThrow(idDoacao);
        doacaoRepository.delete(doacao);
    }
}
