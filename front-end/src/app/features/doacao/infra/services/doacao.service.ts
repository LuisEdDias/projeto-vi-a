import { Injectable } from "@angular/core";
import { BaseService } from "../../../../core/base.service";
import { Observable } from "rxjs";
import { DoacaoCreateDTO, DoacaoResponseDTO, DoacaoWithItensResponseDTO } from "../interfaces/doacao.interface";

@Injectable({
  providedIn: 'root'
})
export class DoacaoService {
    private endpoint: string = 'doacao';

    constructor(
        private baseService: BaseService
    ) { }

    getAll(): Observable<DoacaoResponseDTO[]> {
        return this.baseService.get<DoacaoResponseDTO[]>(this.endpoint);
    }

    getById(id: number): Observable<DoacaoWithItensResponseDTO> {
        return this.baseService.get<DoacaoWithItensResponseDTO>(`${this.endpoint}/${id}`);
    }

    create(data: DoacaoCreateDTO): Observable<DoacaoWithItensResponseDTO> {
        return this.baseService.post<DoacaoWithItensResponseDTO>(this.endpoint, data);
    }

    delete(id: number): Observable<void> {
        return this.baseService.delete<void>(`${this.endpoint}/${id}`);
    }
}