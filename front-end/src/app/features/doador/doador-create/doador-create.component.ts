import { DoadorCreateDTO } from './../infra/interfaces/doador.interface';
import { Component, signal } from '@angular/core';
import { InputComponent } from '../../../shared/input/input.component';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { DoadorService } from '../infra/services/doador.service';

@Component({
  selector: 'app-doador-create',
  imports: [InputComponent, ReactiveFormsModule],
  templateUrl: './doador-create.component.html',
  styleUrl: './doador-create.component.scss',
})
export class DoadorCreateComponent {
  submitted = false;

  createDoadorForm: FormGroup;
  pessoaFisica = true;

  constructor(private doadorService: DoadorService, private fb: FormBuilder) {
    this.createDoadorForm = this.createDoadorFormBuilder();
  }

  createDoadorFormBuilder(): FormGroup {
    return this.fb.group({
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
      docIdentificacao: [
        '',
        [
          Validators.required,
          Validators.pattern(
            this.pessoaFisica
              ? '^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$'
              : '^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$'
          ),
        ],
      ],
    });
  }

  toggleForm(pessoaFisica: boolean): void {
    this.pessoaFisica = pessoaFisica;
    this.createDoadorForm = this.createDoadorFormBuilder();
    this.resetForm();
  }

  createDoador(): void {
    this.submitted = true;
    if (this.createDoadorForm.invalid) {
      return;
    }
    const doador: DoadorCreateDTO = {
      ...this.createDoadorForm.value,
      docIdentificacao: this.createDoadorForm.value.docIdentificacao.replace(
        /\D/g,
        ''
      ),
    };
    this.doadorService.create(doador).subscribe({
      next: () => {
        alert('Doador criado com sucesso!');
        this.createDoadorForm.reset();
        this.submitted = false;
      },
      error: (err) => {
        alert(err['error'] || 'Erro ao criar doador. Tente novamente.');
        this.submitted = false;
      },
    });
  }

  resetForm(): void {
    this.createDoadorForm.reset();
    this.submitted = false;
  }

  getErrorMessage(controlName: string): string {
    const control = this.createDoadorForm.get(controlName);

    if (control?.hasError('required')) {
      return 'Este campo é obrigatório.';
    }

    if (control?.hasError('pattern')) {
      switch (controlName) {
        case 'nome':
          return 'Use apenas letras, números e espaços simples.';
        case 'email':
          return 'Formato de email inválido.';
        case 'docIdentificacao':
          return 'Documento inválido.';
      }
    }

    return '';
  }
}
