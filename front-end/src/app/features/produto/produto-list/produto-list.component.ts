import { Component, signal } from '@angular/core';
import { ProdutoService } from '../infra/services/produto.service';
import { ProdutoResponseDTO } from '../infra/interfaces/produto.interface';
import { NgFor, NgIf } from '@angular/common';
import { ModalComponent } from "../../../shared/modal/modal.component";
import { produtoCategoriaOpt, unidadeControleOpt } from '../infra/util/produto-opt-list';
import { InputComponent } from "../../../shared/input/input.component";
import { SelectComponent } from "../../../shared/select/select.component";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-produto-list',
  imports: [
    NgIf,
    NgFor,
    ModalComponent,
    InputComponent,
    SelectComponent,
    ReactiveFormsModule
],
  templateUrl: './produto-list.component.html',
  styleUrl: './produto-list.component.scss'
})
export class ProdutoListComponent {
  produtos = signal<ProdutoResponseDTO[]>([]);

  produtoCategoriaOpt: Array<{ value: string; label: string }> = produtoCategoriaOpt();
  unidadeControleOpt: Array<{ value: string; label: string }> = unidadeControleOpt();

  submitted = false;
  produtoUpdateId: number | null = null;
  showUpdateProdutoModal = false;
  updateProdutoForm: FormGroup;

  constructor(
    private produtoService: ProdutoService,
    private fb: FormBuilder
  ) {
    this.updateProdutoForm = this.fb.group({
      nome: [
        '', 
        [
          Validators.required, 
          Validators.pattern('^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ0-9]+(\\s?[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ0-9]+)*$')
        ]
      ],
      categoria: ['', Validators.required],
      unidadeControle: ['', Validators.required]
    });
  }

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

  openUpdateProdutoModal(produtoId: number): void {
    this.produtoUpdateId = produtoId;
    const produto: ProdutoResponseDTO | undefined = this.produtos().find(p => p.id === this.produtoUpdateId);
    if (!produto) {
      alert('Produto não encontrado');
      return;
    }
    this.updateProdutoForm.setValue({
      nome: produto.nome,
      categoria: produto.categoria,
      unidadeControle: produto.unidadeControle
    });
    this.showUpdateProdutoModal = true;
  }

  closeUpdateProdutoModal(): void {
    this.showUpdateProdutoModal = false;
    this.produtoUpdateId = null;
    this.updateProdutoForm.reset();
    this.submitted = false;
  }

  updateProduto(): void {
    this.submitted = true;
    if (this.updateProdutoForm.invalid) {
      this.submitted = false;
      return;
    }

    if (this.produtoUpdateId === null) {
      alert('ID do produto não definido');
      this.submitted = false;
      return;
    }

    this.produtoService.update(this.produtoUpdateId, this.updateProdutoForm.value).subscribe({
      next: () => {
        this.loadProdutos();
        this.showUpdateProdutoModal = false;
        this.submitted = false;
      },
      error: () => {
        console.error('Erro ao atualizar produto');
        this.submitted = false;
      }
    });
  }

  removeProduto(id: number): void {
    const produto: ProdutoResponseDTO | undefined = this.produtos().find(p => p.id === id);
    if (!produto) {
      alert('Produto não encontrado');
      return;
    }

    const confirmRemoval = confirm(`Tem certeza que deseja remover o produto "${produto.nome}"?`);

    if (!confirmRemoval) return;

    this.produtoService.delete(id).subscribe({
      next: () => {
        this.loadProdutos();
      },
      error: () => {
        console.error('Erro ao remover produto');
      }
    });
  }

  getErrorMessage(controlName: string): string {
    const control = this.updateProdutoForm.get(controlName);

    if (control?.hasError('required')) {
      return 'Este campo é obrigatório.';
    }

    if (control?.hasError('pattern')) {
      return 'Use apenas letras, números e espaços simples.';
    }

    return '';
  }
}
