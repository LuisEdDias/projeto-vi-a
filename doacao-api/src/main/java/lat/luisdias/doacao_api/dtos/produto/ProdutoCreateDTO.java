package lat.luisdias.doacao_api.dtos.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lat.luisdias.doacao_api.entities.ProdutoCategoria;
import lat.luisdias.doacao_api.entities.UnidadeControle;

// DTO usado para receber os dados de criação do Produto com validações iniciais
public record ProdutoCreateDTO(
        @NotBlank(message = "Campo obrigatório")
        @Pattern(
                regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ0-9]+(\\s?[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ0-9]+)*$",
                message = "Use apenas letras, números e espaços simples"
        )
        String nome,
        @NotNull(message = "Campo obrigatório")
        ProdutoCategoria categoria,
        @NotNull(message = "Campo obrigatório")
        UnidadeControle unidadeControle
) {
}
