import { Component, signal } from '@angular/core';
import { ModalComponent } from '../../../shared/modal/modal.component';
import { InputComponent } from '../../../shared/input/input.component';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  DoadorWithDoacoesResponseDTO,
} from '../infra/interfaces/doador.interface';
import { DoadorService } from '../infra/services/doador.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { DoacaoService } from '../../doacao/infra/services/doacao.service';
import { NgxMaskPipe } from 'ngx-mask';

@Component({
  selector: 'app-doador-details',
  imports: [
    ModalComponent, 
    InputComponent, 
    ReactiveFormsModule, 
    RouterLink,
    NgxMaskPipe
  ],
  templateUrl: './doador-details.component.html',
  styleUrl: './doador-details.component.scss',
})
export class DoadorDetailsComponent {
  doadorId: number;
  doador = signal<DoadorWithDoacoesResponseDTO | null>(null);

  showUpdateDoadorModal = false;
  updateDoadorForm: FormGroup;
  submitted = false;

  constructor(
    private doadorService: DoadorService,
    private route: ActivatedRoute,
    private router: Router,
    private doacaoService: DoacaoService,
    private fb: FormBuilder
  ) {
    this.doadorId = Number(this.route.snapshot.paramMap.get('id'));
    this.updateDoadorForm = this.fb.group({
      nome: [
        '',
        [
          Validators.required,
          Validators.pattern(
            '^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ0-9]+(\\s?[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ0-9]+)*$'
          ),
        ],
      ],
      email: [
        '',
        [
          Validators.required,
          Validators.pattern(
            '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$'
          ),
        ],
      ],
    });
  }

  ngOnInit(): void {
    this.loadDoador();
  }

  loadDoador() {
    this.doadorService.getById(this.doadorId).subscribe({
      next: (data) => {
        this.doador.set(data);
      },
      error: (err) => {
        console.error('Erro ao carregar a doação:', err);
        alert('Erro ao carregar a doação. Redirecionando para a lista.');
        this.router.navigate(['/doador']);
      },
    });
  }

  deleteDoador() {
    if ((this.doador()?.doacoes?.length ?? 0) > 0) {
      alert(
        'Você precisa remover todas as doações associadas ao doador antes de removê-lo.'
      );
      return;
    }

    if (
      confirm(
        `
        Tem certeza que deseja excluir este doador?\n
        Nome: ${this.doador()?.nome}\n
        Doc. Identificação: ${this.doador()?.docIdentificacao}
        `
      )
    ) {
      this.doadorService.delete(this.doadorId).subscribe({
        next: () => {
          alert('Registro removido com sucesso!');
          this.router.navigate(['/doador']);
        },
        error: (err) => {
          alert(
            err['error'] || 'Erro ao excluir dados. Por favor, tente novamente.'
          );
          console.error('Erro ao excluir doador:', err);
        },
      });
    }
  }

  updateDoadorModal() {
    this.submitted = false;
    this.updateDoadorForm.setValue({
      nome: this.doador()?.nome,
      email: this.doador()?.email,
    });
    this.showUpdateDoadorModal = true;
  }

  updateDoador(): void {
    this.submitted = true;
    if (this.updateDoadorForm.invalid) {
      return;
    }

    this.doadorService
      .update(this.doadorId, this.updateDoadorForm.value)
      .subscribe({
        next: () => {
          alert('Dados do doador atualizados com sucesso!');
          this.showUpdateDoadorModal = false;
          this.submitted = false;
          this.loadDoador();
        },
        error: (err) => {
          alert(err['error'] || 'Erro ao atualizar dados. Tente novamente.');
          this.submitted = false;
        },
      });
  }

  removeDoacao(id: number): void {
    const doacao = this.doador()?.doacoes.find((d) => d.id === id);
    if (!doacao) {
      alert('Doação não encontrada.');
      return;
    }
    if (
      confirm(
        `
        Tem certeza que deseja excluir esta doação?\n
        ID: ${doacao.id}\n
        Comentário: ${doacao.descricao}\n
        Data: ${doacao.data}\n
        `
      )
    ) {
      this.doacaoService.delete(id).subscribe({
        next: () => {
          this.loadDoador();
        },
        error: (err) => {
          alert(
            err['error'] ||
              'Erro ao excluir doação. Por favor, tente novamente.'
          );
          console.error('Erro ao excluir doação:', err);
        },
      });
    }
  }

  getErrorMessage(controlName: string): string {
    const control = this.updateDoadorForm.get(controlName);

    if (control?.hasError('required')) {
      return 'Este campo é obrigatório.';
    }

    if (control?.hasError('pattern')) {
      switch (controlName) {
        case 'nome':
          return 'Use apenas letras, números e espaços simples.';
        case 'email':
          return 'Formato de email inválido.';
      }
    }

    return '';
  }
}
