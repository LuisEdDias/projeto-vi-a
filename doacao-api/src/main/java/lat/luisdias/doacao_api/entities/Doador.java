package lat.luisdias.doacao_api.entities;

import jakarta.persistence.*;
import lat.luisdias.doacao_api.dtos.DoadorCreateDTO;
import lat.luisdias.doacao_api.dtos.DoadorUpdateDTO;

// Classe que representa um doador no sistema
@Entity(name = "doador")
public class Doador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String docIdentificacao;

    // Construtor padrão exigido pela JPA
    protected Doador() {}

    // Construtor que recebe os dados
    public Doador(DoadorCreateDTO doadorCreateDTO) {
        this.nome = doadorCreateDTO.getNome();
        this.email = doadorCreateDTO.getEmail();
        this.docIdentificacao = doadorCreateDTO.getDocIdentificacao();
    }

    // Método para atualizar os dados de um doador
    public void update(DoadorUpdateDTO doadorUpdateDTO) {
        if (doadorUpdateDTO.nome() != null){
            this.nome = doadorUpdateDTO.nome();
        }

        if (doadorUpdateDTO.email() != null){
            this.email = doadorUpdateDTO.email();
        }
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getDocIdentificacao() {
        return docIdentificacao;
    }
}
