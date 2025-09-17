package lat.luisdias.doacao_api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record DoadorUpdateDTO(
        @Pattern(
                regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]+(\\s?[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]+)*$",
                message = "Use apenas letras e espaços simples"
        )
        String nome,
        @Email(message = "O email precisa ter um formato válido")
        String email
) {
}
