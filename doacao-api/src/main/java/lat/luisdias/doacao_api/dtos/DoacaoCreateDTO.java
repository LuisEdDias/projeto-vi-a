package lat.luisdias.doacao_api.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

// DTO para receber e validar os dados de uma doação
public record DoacaoCreateDTO(
        @NotNull(message = "É obrigatório informar o doador")
        Long doadorId,
        String descricao,
        @NotEmpty(message = "Precisa conter itens válidos")
        List<ProdutoQuantidade> itens
) {
    public record ProdutoQuantidade(
            @NotNull(message = "É obrigatório informar o produto")
            Long produtoId,
            @NotNull(message = "A quantidade precisa ser especificada")
            @DecimalMin(value = "0.01", message = "A quantidade deve ser maior que zero")
            @Digits(integer = 10, fraction=2, message = "Formato inválido para quantidade")
            BigDecimal quantidade
    ){}
}
