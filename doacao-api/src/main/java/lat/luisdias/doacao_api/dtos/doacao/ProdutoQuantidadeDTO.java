package lat.luisdias.doacao_api.dtos.doacao;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoQuantidadeDTO(
        @NotNull(message = "É obrigatório informar o produto")
        Long produtoId,
        @NotNull(message = "A quantidade precisa ser especificada")
        @DecimalMin(value = "0.01", message = "A quantidade deve ser maior que zero")
        @Digits(integer = 10, fraction=2, message = "Formato inválido para quantidade")
        BigDecimal quantidade
) {
}
