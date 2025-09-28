import { Component, signal } from '@angular/core';
import {
  DoacaoWithItensResponseDTO,
  ProdutoQuantidadeDTO,
} from '../infra/interfaces/doacao.interface';
import { DoacaoService } from '../infra/services/doacao.service';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterLink } from '@angular/router';
import { DecimalPipe, NgFor } from '@angular/common';
import { ModalComponent } from '../../../shared/modal/modal.component';
import { InputComponent } from '../../../shared/input/input.component';
import { SelectComponent } from '../../../shared/select/select.component';
import { ProdutoService } from '../../produto/infra/services/produto.service';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

@Component({
  selector: 'app-doacao-details',
  imports: [
    RouterLink,
    NgFor,
    ModalComponent,
    InputComponent,
    SelectComponent,
    ReactiveFormsModule,
    DecimalPipe
  ],
  templateUrl: './doacao-details.component.html',
  styleUrl: './doacao-details.component.scss',
})
export class DoacaoDetailsComponent {
  doacaoId: number;
  doacao = signal<DoacaoWithItensResponseDTO | null>(null);

  produtoOptList: { label: string; value: number }[] = [];
  showAddItemModal = false;
  addItemForm: FormGroup;
  submitted = false;

  constructor(
    private doacaoService: DoacaoService,
    private produtoService: ProdutoService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.doacaoId = Number(this.route.snapshot.paramMap.get('id'));
    this.addItemForm = this.fb.group({
      produtoId: ['', Validators.required],
      quantidade: ['', [Validators.required, Validators.min(0.1)]],
    });
  }

  ngOnInit(): void {
    this.loadDoacaoDetails(this.doacaoId);
    this.produtoService.getAll().subscribe({
      next: (data) => {
        this.produtoOptList = data.map((produto) => ({
          label: produto.nome,
          value: produto.id,
        }));
      },
      error: (error) => {
        console.error('Erro ao carregar produtos:', error);
      },
    });
  }

  loadDoacaoDetails(id: number): void {
    this.doacaoService.getById(id).subscribe({
      next: (data) => {
        this.doacao.set(data);
      },
      error: (error) => {
        console.error('Erro ao carregar a doação:', error);
        alert('Erro ao carregar a doação. Redirecionando para a lista.');
        this.router.navigate(['/doacao']);
      },
    });
  }

  addItem(): void {
    this.submitted = true;

    if (this.addItemForm.invalid) {
      return;
    }
    if (!this.doacaoId) {
      console.error('ID da doação não definido.');
      return;
    }

    const item: ProdutoQuantidadeDTO = {
      produtoId: this.addItemForm.value.produtoId,
      quantidade: Number(
        (this.addItemForm.get('quantidade')?.value || '0').replace(',', '.')
      ),
    };

    this.doacaoService.addItem(this.doacaoId, item).subscribe({
      next: () => {
        alert('Item adicionado com sucesso.');
        this.loadDoacaoDetails(this.doacaoId);
        this.addItemForm.reset();
        this.submitted = false;
        this.showAddItemModal = false;
      },
      error: (err) => {
        console.error('Erro ao adicionar o item:', err);
        alert(
          err['error'] ||
            'Erro ao adicionar o item. Tente novamente mais tarde.'
        );
      },
    });
  }

  closeAddItemModal(): void {
    this.showAddItemModal = false;
    this.addItemForm.reset();
    this.submitted = false;
  }

  removeItem(itemId: number): void {
    if (this.doacao()?.itens.length === 1) {
      alert(
        'Não é possível remover o último item. Delete a doação se desejar removê-la completamente.'
      );
      return;
    }

    if (!this.doacaoId) {
      console.error('ID da doação não definido.');
      return;
    }

    if (confirm('Tem certeza que deseja remover este item da doação?')) {
      this.doacaoService.removeItem(this.doacaoId, itemId).subscribe({
        next: () => {
          alert('Item removido com sucesso.');
          this.loadDoacaoDetails(this.doacaoId);
        },
        error: (err) => {
          console.error('Erro ao remover o item:', err);
          alert(
            err['error'] ||
              'Erro ao remover o item. Tente novamente mais tarde.'
          );
        },
      });
    }
  }

  deleteDoacao(): void {
    if (!this.doacaoId) {
      console.error('ID da doação não definido.');
      return;
    }
    if (confirm('Tem certeza que deseja deletar esta doação?')) {
      this.doacaoService.delete(this.doacaoId).subscribe({
        next: () => {
          alert('Doação deletada com sucesso.');
          this.router.navigate(['/doacao']);
        },
        error: (err) => {
          console.error('Erro ao deletar a doação:', err);
          alert(
            err['error'] ||
              'Erro ao deletar a doação. Tente novamente mais tarde.'
          );
        },
      });
    }
  }

  getErrorMessage(controlName: string): string {
    const control = this.addItemForm.get(controlName);

    if (control?.hasError('required')) {
      return 'Este campo é obrigatório.';
    }

    if (control?.hasError('min')) {
      return 'O valor deve ser maior que zero.';
    }

    return '';
  }
}
