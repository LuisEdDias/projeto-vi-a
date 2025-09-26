import { Injectable } from "@angular/core";
import { BaseService } from "../../../core/base.service";
import { ProdutoResponseDTO } from "../interfaces/produto.interface";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class ProdutoService {
  private endpoint: string = "produto";

    constructor(private baseService: BaseService) {}

    getAll(): Observable<ProdutoResponseDTO[]> {
        return this.baseService.get<ProdutoResponseDTO[]>(this.endpoint);
    }
}
