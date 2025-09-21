package lat.luisdias.doacao_api.dtos.produto;

import jakarta.validation.constraints.Pattern;
import lat.luisdias.doacao_api.entities.ProdutoCategoria;
import lat.luisdias.doacao_api.entities.UnidadeControle;

// DTO usado para receber os dados de atualização do Produto com validações iniciais
public record ProdutoUpdateDTO(
        @Pattern(
                regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ0-9]+(\\s?[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ0-9]+)*$",
                message = "Use apenas letras, números e espaços simples"
        )
        String nome,
        ProdutoCategoria categoria,
        UnidadeControle unidadeControle
) {
}
