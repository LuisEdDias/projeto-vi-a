package lat.luisdias.doacao_api.dtos.doador;

import lat.luisdias.doacao_api.entities.Doador;

public record DoadorGetDTO(
        Long id,
        String nome,
        String email,
        String docIdentificacao
) {
    public DoadorGetDTO(Doador doador) {
        this(
                doador.getId(),
                doador.getNome(),
                doador.getEmail(),
                doador.getDocIdentificacao()
        );
    }
}
