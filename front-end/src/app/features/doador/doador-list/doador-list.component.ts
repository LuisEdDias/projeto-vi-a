import { Component, signal } from '@angular/core';
import { ModalComponent } from '../../../shared/modal/modal.component';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { DoadorResponseDTO } from '../infra/interfaces/doador.interface';
import { DoadorService } from '../infra/services/doador.service';
import { InputComponent } from '../../../shared/input/input.component';
import { NgFor, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-doador-list',
  imports: [
    ModalComponent,
    ReactiveFormsModule,
    InputComponent,
    NgIf,
    NgFor,
    RouterLink
],
  templateUrl: './doador-list.component.html',
  styleUrl: './doador-list.component.scss',
})
export class DoadorListComponent {
  doadores = signal<DoadorResponseDTO[]>([]);

  submitted = false;
  doadorUpdateId: number | null = null;
  showUpdateDoadorModal = false;
  updateDoadorForm: FormGroup;

  constructor(
    private doadorService: DoadorService, 
    private fb: FormBuilder
  ) {
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
    this.loadDoadores();
  }

  loadDoadores(): void {
    this.doadorService.getAll().subscribe({
      next: (data) => {
        this.doadores.set(data);
      },
      error: (err) => {
        alert(err['error'] || 'Erro ao carregar doadores');
        console.error(err);
      }
    });
  }

  openUpdateDoadorModal(doadorId: number): void {
    this.doadorUpdateId = doadorId;
    const doador = this.doadores().find(d => d.id === doadorId);
    if (!doador) {
      alert('Doador não encontrado');
      return;
    }
    this.updateDoadorForm.setValue({
      nome: doador.nome,
      email: doador.email
    });
    this.showUpdateDoadorModal = true;
  }

  closeUpdateDoadorModal(): void {
    this.showUpdateDoadorModal = false;
    this.doadorUpdateId = null;
    this.updateDoadorForm.reset();
    this.submitted = false;
  }

  updateDoador(): void {
    this.submitted = true;
    if (this.updateDoadorForm.invalid) {
      return;
    }
    if (this.doadorUpdateId === null) {
      alert('ID do doador não definido');
      this.submitted = false;
      return;
    }

    this.doadorService.update(this.doadorUpdateId, this.updateDoadorForm.value).subscribe({
      next: () => {
        alert('Doador atualizado com sucesso');
        this.loadDoadores();
        this.showUpdateDoadorModal = false;
        this.submitted = false;
      },
      error: (err) => {
        alert(err['error'] || 'Erro ao atualizar doador');
        console.error(err);
        this.submitted = false;
      }
    });
  }

  removeDoador(doadorId: number): void {
    const doador = this.doadores().find(d => d.id === doadorId);
    if (!doador) {
      alert('Doador não encontrado');
      return;
    }
    if (!confirm(`Tem certeza que deseja remover o doador ${doador.nome}?`)) {
      return;
    }

    this.doadorService.delete(doadorId).subscribe({
      next: () => {
        this.loadDoadores();
      },
      error: (err) => {
        alert(err['error'] || 'Erro ao remover doador');
        console.error(err);
      }
    });
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
          return 'Email inválido.';
      }
    }

    return '';
  }
}
