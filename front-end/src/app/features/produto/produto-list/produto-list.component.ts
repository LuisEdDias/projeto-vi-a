import { Component, signal } from '@angular/core';
import { ProdutoService } from '../services/produto.service';
import { ProdutoResponseDTO } from '../interfaces/produto.interface';
import { NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-produto-list',
  imports: [
    NgIf,
    NgFor
  ],
  templateUrl: './produto-list.component.html',
  styleUrl: './produto-list.component.scss'
})
export class ProdutoListComponent {
  produtos = signal<ProdutoResponseDTO[]>([]);
  
  constructor(
    private produtoService: ProdutoService
  ) { }

  ngOnInit(): void {
    this.loadProdutos();
  }

  loadProdutos(): void {
    this.produtoService.getAll().subscribe({
      next: (data) => {
        this.produtos.set(data);
      },
      error: () => {
        console.error('Erro ao carregar produtos');
      }
    });
  }
}
