import { Injectable } from '@angular/core';
import { BaseService } from '../../../../core/base.service';
import {
  ProdutoCreateDTO,
  ProdutoResponseDTO,
} from '../interfaces/produto.interface';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProdutoService {
  private endpoint: string = 'produto';

  constructor(private baseService: BaseService) {}

  getAll(): Observable<ProdutoResponseDTO[]> {
    return this.baseService.get<ProdutoResponseDTO[]>(this.endpoint);
  }

  create(data: ProdutoCreateDTO): Observable<ProdutoResponseDTO> {
    return this.baseService.post<ProdutoResponseDTO>(this.endpoint, data);
  }

  update(id: number, data: ProdutoCreateDTO): Observable<ProdutoResponseDTO> {
    return this.baseService.put<ProdutoResponseDTO>(
      `${this.endpoint}/${id}`,
      data
    );
  }

  delete(id: number): Observable<void> {
    return this.baseService.delete<void>(`${this.endpoint}/${id}`);
  }
}
