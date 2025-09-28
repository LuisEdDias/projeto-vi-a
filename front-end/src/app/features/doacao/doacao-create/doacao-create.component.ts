import { Component, signal } from '@angular/core';
import { InputComponent } from '../../../shared/input/input.component';
import { SelectComponent } from '../../../shared/select/select.component';
import { TextareaComponent } from '../../../shared/textarea/textarea.component';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DoacaoService } from '../infra/services/doacao.service';
import { DoadorService } from '../../doador/infra/services/doador.service';
import { ProdutoService } from '../../produto/infra/services/produto.service';
import { DoacaoCreateDTO, ProdutoQuantidadeDTO } from '../infra/interfaces/doacao.interface';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-doacao-create',
  imports: [
    InputComponent,
    SelectComponent,
    TextareaComponent,
    ReactiveFormsModule,
    DecimalPipe
  ],
  templateUrl: './doacao-create.component.html',
  styleUrl: './doacao-create.component.scss',
})
export class DoacaoCreateComponent {
  submitted = false;
  addItemSubmitted = false;

  doadorOptList: Array<{ label: string; value: number }> = [];
  produtoOptList: Array<{ label: string; value: number }> = [];

  createDoacaoForm: FormGroup;
  addItemForm: FormGroup;

  produtoQuantidade = signal<ProdutoQuantidadeDTO[]>([]);

  constructor(
    private doacaoService: DoacaoService,
    private produtoService: ProdutoService,
    private doadorService: DoadorService,
    private fb: FormBuilder
  ) {
    this.createDoacaoForm = this.fb.group({
      doadorId: ['', Validators.required],
      data: ['', Validators.required],
      descricao: ['', Validators.maxLength(500)],
    });
    this.addItemForm = this.fb.group({
      produtoId: ['', Validators.required],
      quantidade: ['', [Validators.required, Validators.min(0.1)]],
    });
  }

  ngOnInit(): void {
    this.loadOptions();
  }

  loadOptions(): void {
    this.doadorService.getAll().subscribe({
      next: (data) => {
        this.doadorOptList = data.map((doador) => {
          const doc =
            doador.docIdentificacao.length > 11
              ? doador.docIdentificacao.replace(
                  /^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/,
                  '$1.$2.$3/$4-$5'
                )
              : doador.docIdentificacao.replace(
                  /^(\d{3})(\d{3})(\d{3})(\d{2})$/,
                  '$1.$2.$3-$4'
                );
          return {
            label: `${doador.nome} | Doc: ${doc}`,
            value: doador.id,
          };
        });
      },
      error: (err) => {
        console.error(err);
      },
    });
    this.produtoService.getAll().subscribe({
      next: (data) => {
        this.produtoOptList = data.map((produto) => ({
          label: produto.nome,
          value: produto.id,
        }));
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  addItem(): void {
    this.addItemSubmitted = true;
    if (this.addItemForm.invalid) {
      return;
    }

    const produtoId = this.addItemForm.get('produtoId')?.value;
    const quantidade = Number((this.addItemForm.get('quantidade')?.value || '0').replace(',', '.'));

    const produto = this.produtoOptList.find((p) => p.value === produtoId);
    if (!produto) {
      return;
    }

    const currentItems = this.produtoQuantidade();
    const existingItemIndex = currentItems.findIndex(
      (item) => item.produtoId === produtoId
    );

    if (existingItemIndex !== -1) {
      currentItems[existingItemIndex].quantidade += quantidade;
    } else {
      currentItems.push({
        produtoId,
        quantidade,
      });
    }
    this.produtoQuantidade.set(currentItems);
    this.addItemForm.reset();
    this.addItemSubmitted = false;
  }

  removeItem(produtoId: number): void {
    const updatedItems = this.produtoQuantidade().filter(
      (item) => item.produtoId !== produtoId
    );
    this.produtoQuantidade.set(updatedItems);
  }

  clearAllItems(): void {
    this.produtoQuantidade.set([]);
  }

  getProdutoNome(produtoId: number): string {
    const produto = this.produtoOptList.find((p) => p.value === produtoId);
    return produto ? produto.label : 'Produto não encontrado';
  }

  createDoacao(): void {
    this.submitted = true;
    if (this.createDoacaoForm.invalid) {
      return;
    }
    const doacaoData: DoacaoCreateDTO = {
      doadorId: this.createDoacaoForm.get('doadorId')?.value,
      data: this.createDoacaoForm.get('data')?.value,
      descricao: this.createDoacaoForm.get('descricao')?.value,
      itens: this.produtoQuantidade(),
    };
    this.doacaoService.create(doacaoData).subscribe({
      next: (data) => {
        alert('Doação criada com sucesso!');
        console.log('Doação criada com sucesso:', data);
        this.createDoacaoForm.reset();
        this.produtoQuantidade.set([]);
        this.submitted = false;
      }, 
      error: (err) => {
        alert(err['error'] || 'Erro ao criar doação. Por favor, tente novamente.');
        console.error(err);
      },
    });
  }

  getErrorMessage(controlName: string, form: FormGroup): string {
    const control = form.get(controlName);

    if (control?.hasError('required')) {
      return 'Este campo é obrigatório.';
    }

    if (control?.hasError('min')) {
      return 'O valor deve ser maior que zero.';
    }

    if (control?.hasError('maxLength')) {
      return 'O tamanho máximo permitido é de 500 caracteres.';
    }

    return '';
  }
}
