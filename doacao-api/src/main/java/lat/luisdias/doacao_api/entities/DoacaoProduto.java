package lat.luisdias.doacao_api.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

// Entidade associativa entre Produto e Doação
@Entity(name = "doacao_produto")
public class DoacaoProduto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal quantidade;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "doacao_id", nullable = false)
    private Doacao doacao;

    // Construtor padrão exigido pela JPA
    protected DoacaoProduto() {}

    // Construtor que recebe os dados
    public DoacaoProduto(BigDecimal quantidade, Produto produto, Doacao doacao) {
        this.quantidade = quantidade;
        this.produto = produto;
        this.doacao = doacao;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public Doacao getDoacao() {
        return doacao;
    }
}
