package lat.luisdias.doacao_api.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// DTO usado para receber os dados de criação do Doador com validações iniciais
public class DoadorCreateDTO{
        @NotBlank(message = "Campo obrigatório")
        @Pattern(
                regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]+(\\s?[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]+)*$",
                message = "Use apenas letras e espaços simples"
        )
        private String nome;
        @NotBlank(message = "Campo obrigatório")
        @Email(message = "O email precisa ter um formato válido")
        private String email;
        @NotBlank(message = "Campo obrigatório")
        @Pattern(
                regexp = "^\\d*$",
                message = "Use apenas números"
        )
        private String docIdentificacao;

        @AssertTrue(message = "Deve ser um CPF/CNPJ válido")
        private boolean docIsValid(){
                return docIdentificacao.length() == 11 || docIdentificacao.length() == 14;
        }

        public String getNome() {
                return nome;
        }

        public void setNome() {
                this.nome = nome;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail() {
                this.email = email;
        }

        public String getDocIdentificacao() {
                return docIdentificacao;
        }

        public void setDoc_identificacao() {
                this.docIdentificacao = docIdentificacao;
        }
}
