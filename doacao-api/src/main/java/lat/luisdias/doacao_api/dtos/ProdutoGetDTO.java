package lat.luisdias.doacao_api.dtos;

import lat.luisdias.doacao_api.entities.Produto;

public record ProdutoGetDTO(
        Long id,
        String nome,
        String categoria,
        String unidadeControle
) {
    public ProdutoGetDTO(Produto produto) {
        this(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria().name(),
                produto.getUnidadeControle().name()
        );
    }
}
