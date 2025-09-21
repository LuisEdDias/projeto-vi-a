package lat.luisdias.doacao_api.dtos.doador;

import lat.luisdias.doacao_api.entities.Doacao;
import lat.luisdias.doacao_api.entities.Doador;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record DoadorWithDoacoesGetDTO(
        Long id,
        String nome,
        String email,
        String docIdentificacao,
        List<DoacaoDTO> doacoes
) {
    public DoadorWithDoacoesGetDTO(Doador doador) {
        this(
                doador.getId(),
                doador.getNome(),
                doador.getEmail(),
                doador.getDocIdentificacao(),
                doador.getDoacoes().stream().map(DoacaoDTO::new).toList()
        );
    }
}

record DoacaoDTO(
        Long id,
        String descricao,
        String data
) {
    public DoacaoDTO(Doacao doacao){
        this(
                doacao.getId(),
                doacao.getDescricao(),
                doacao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
    }
}
