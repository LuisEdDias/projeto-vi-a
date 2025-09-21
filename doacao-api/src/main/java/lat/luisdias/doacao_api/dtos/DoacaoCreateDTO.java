package lat.luisdias.doacao_api.dtos;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

// DTO para receber e validar os dados de uma doação
public record DoacaoCreateDTO(
        @NotNull(message = "É obrigatório informar o doador")
        Long doadorId,
        String descricao,

        LocalDate data,
        @NotEmpty(message = "Precisa conter itens válidos")
        List<ProdutoQuantidadeDTO> itens
) {
}
