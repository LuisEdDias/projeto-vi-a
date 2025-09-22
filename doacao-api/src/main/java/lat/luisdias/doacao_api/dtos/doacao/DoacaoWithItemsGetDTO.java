package lat.luisdias.doacao_api.dtos.doacao;

import lat.luisdias.doacao_api.dtos.doador.DoadorGetDTO;
import lat.luisdias.doacao_api.entities.Doacao;
import lat.luisdias.doacao_api.entities.DoacaoProduto;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record DoacaoWithItemsGetDTO(
        Long doacaoId,
        String descricao,
        String data,
        DoadorGetDTO doador,
        List<DoacaoProdutoGetDTO> itens

) {
    public DoacaoWithItemsGetDTO(Doacao doacao) {
        this(
                doacao.getId(),
                doacao.getDescricao(),
                doacao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                new DoadorGetDTO(doacao.getDoador()),
                doacao.getItens().stream().map(DoacaoProdutoGetDTO::new).toList()
        );
    }
}

record DoacaoProdutoGetDTO(
        Long id,
        Long produtoId,
        String produtoNome,
        String produtoCategoria,
        String produtoUnidadeControle,
        BigDecimal quantidade
) {
    public DoacaoProdutoGetDTO(DoacaoProduto doacaoProduto) {
        this(
                doacaoProduto.getId(),
                doacaoProduto.getProduto().getId(),
                doacaoProduto.getProduto().getNome(),
                doacaoProduto.getProduto().getCategoria().name(),
                doacaoProduto.getProduto().getUnidadeControle().name(),
                doacaoProduto.getQuantidade()
        );
    }
}
