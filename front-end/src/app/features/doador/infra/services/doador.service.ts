import { Injectable } from '@angular/core';
import { BaseService } from '../../../../core/base.service';
import { Observable } from 'rxjs';
import { DoadorCreateDTO, DoadorResponseDTO } from '../interfaces/doador.interface';

@Injectable({
  providedIn: 'root',
})
export class DoadorService {
  private endpoint: string = 'doador';

  constructor(private baseService: BaseService) {}

  getAll(): Observable<DoadorResponseDTO[]> {
    return this.baseService.get<DoadorResponseDTO[]>(this.endpoint);
  }

  create(data: DoadorCreateDTO): Observable<DoadorResponseDTO> {
    return this.baseService.post<DoadorResponseDTO>(this.endpoint, data);
  }
}
