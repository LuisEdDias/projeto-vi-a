package lat.luisdias.doacao_api.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

// Classe que representa uma doação
@Entity(name = "doacao")
public class Doacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    @Column(nullable = false)
    private LocalDate data;

    // Relacionamento bidirecional com a entidade Doador
    @ManyToOne
    @JoinColumn(name = "doador_id", nullable = false)
    private Doador doador;

    // Relacionamento bidirecional com a entidade DoacaoProduto
    @OneToMany(mappedBy = "doacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoacaoProduto> itens;

    // Construtor padrão exigido pela JPA
    protected Doacao() {}

    // Construtor que recebe os dados da doação
    public Doacao(String descricao, LocalDate data, Doador doador, List<DoacaoProduto> itens) {
        this.descricao = descricao;
        this.data = data;
        this.doador = doador;
        this.itens = itens;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getData() {
        return data;
    }

    public Doador getDoador() {
        return doador;
    }

    public List<DoacaoProduto> getItens() {
        return itens;
    }
}
