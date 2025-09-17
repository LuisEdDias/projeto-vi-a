package lat.luisdias.doacao_api.entities;

import jakarta.persistence.*;
import lat.luisdias.doacao_api.dtos.ProdutoCreateDTO;
import lat.luisdias.doacao_api.dtos.ProdutoUpdateDTO;

@Entity(name = "produto")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private ProdutoCategoria categoria;
    @Column(nullable = false)
    private UnidadeControle unidadeControle;

    protected Produto() {}

    public Produto(ProdutoCreateDTO produtoCreateDTO) {
        this.nome = produtoCreateDTO.nome();
        this.categoria = produtoCreateDTO.categoria();
        this.unidadeControle = produtoCreateDTO.unidadeControle();
    }

    public void update(ProdutoUpdateDTO produtoUpdateDTO) {
        if (produtoUpdateDTO.nome() != null) {
            this.nome = produtoUpdateDTO.nome();
        }

        if (produtoUpdateDTO.categoria() != null) {
            this.categoria = produtoUpdateDTO.categoria();
        }

        if (produtoUpdateDTO.unidadeControle() != null) {
            this.unidadeControle = produtoUpdateDTO.unidadeControle();
        }
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public ProdutoCategoria getCategoria() {
        return categoria;
    }

    public UnidadeControle getUnidadeControle() {
        return unidadeControle;
    }
}
