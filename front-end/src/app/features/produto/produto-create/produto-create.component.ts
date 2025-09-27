import { Component } from '@angular/core';
import { InputComponent } from "../../../shared/input/input.component";
import { SelectComponent } from "../../../shared/select/select.component";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { produtoCategoriaOpt, unidadeControleOpt } from '../infra/util/produto-opt-list';
import { ProdutoService } from '../infra/services/produto.service';

@Component({
  selector: 'app-produto-create',
  imports: [
    InputComponent, 
    SelectComponent,
    ReactiveFormsModule
  ],
  templateUrl: './produto-create.component.html',
  styleUrl: './produto-create.component.scss'
})
export class ProdutoCreateComponent {
  createProdutoForm: FormGroup;
  submitted = false;

  produtoCategoriaOpt: Array<{ value: string; label: string }> = produtoCategoriaOpt();
  unidadeControleOpt: Array<{ value: string; label: string }> = unidadeControleOpt();

  constructor(
    private fb: FormBuilder,
    private produtoService: ProdutoService
  ) {
    this.createProdutoForm = this.fb.group({
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

  createProduto(): void {
    this.submitted = true;

    if (this.createProdutoForm.invalid) {
      return;
    }

    this.produtoService.create(this.createProdutoForm.value).subscribe({
      next: (data) => {
        alert('Produto criado com sucesso!');
        this.createProdutoForm.reset();
        this.submitted = false;
      },
      error: (err) => {
        alert(err['error'] || 'Erro ao criar produto. Tente novamente.');
        console.error(err);
        this.submitted = false;
      }
    });
  }

  resetForm(): void {
    this.createProdutoForm.reset();
    this.submitted = false;
  }

  getErrorMessage(controlName: string): string {
    const control = this.createProdutoForm.get(controlName);

    if (control?.hasError('required')) {
      return 'Este campo é obrigatório.';
    }

    if (control?.hasError('pattern')) {
      return 'Use apenas letras, números e espaços simples.';
    }

    return '';
  }
}
