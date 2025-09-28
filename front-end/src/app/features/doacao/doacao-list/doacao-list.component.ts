import { Component, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import { DoacaoResponseDTO } from '../infra/interfaces/doacao.interface';
import { DoacaoService } from '../infra/services/doacao.service';

@Component({
  selector: 'app-doacao-list',
  imports: [ReactiveFormsModule, NgIf, NgFor, RouterLink],
  templateUrl: './doacao-list.component.html',
  styleUrl: './doacao-list.component.scss',
})
export class DoacaoListComponent {
  doacaoList = signal<DoacaoResponseDTO[]>([]);

  constructor(private doacaoService: DoacaoService) {}

  ngOnInit(): void {
    this.loadDoacoes();
  }

  loadDoacoes(): void {
    this.doacaoService.getAll().subscribe({
      next: (data) => {
        this.doacaoList.set(data);
      },
      error: (err) => {
        alert(
          err['error'] ||
            'Erro ao carregar doações. Por favor, tente novamente.'
        );
        console.error('Erro ao carregar doações:', err);
      },
    });
  }

  removeDoacao(id: number): void {
    const doacao = this.doacaoList().find((d) => d.doacaoId === id);
    if (!doacao) {
      alert('Doação não encontrada.');
      return;
    }
    if (
      confirm(
        `
        Tem certeza que deseja excluir esta doação?\n
        ID: ${doacao.doacaoId}\n
        Comentário: ${doacao.descricao}\n
        Doador: ${doacao.doadorNome}\n
        Data: ${doacao.data}\n
        `
      )
    ) {
      this.doacaoService.delete(id).subscribe({
        next: () => {
          this.loadDoacoes();
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
}
