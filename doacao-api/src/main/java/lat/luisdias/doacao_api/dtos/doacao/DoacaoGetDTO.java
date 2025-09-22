package lat.luisdias.doacao_api.dtos.doacao;

import lat.luisdias.doacao_api.entities.Doacao;

import java.time.format.DateTimeFormatter;

public record DoacaoGetDTO(
        Long doacaoId,
        String descricao,
        String data,
        Long doadorId,
        String doadorNome
) {
    public DoacaoGetDTO(Doacao doacao) {
        this(
                doacao.getId(),
                doacao.getDescricao(),
                doacao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                doacao.getDoador().getId(),
                doacao.getDoador().getNome()
        );
    }
}
