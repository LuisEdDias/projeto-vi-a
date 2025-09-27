export interface ProdutoResponseDTO {
  id: number;
  nome: string;
  categoria: string;
  unidadeControle: string;
}

export interface ProdutoCreateDTO {
  nome: string;
  categoria: string;
  unidadeControle: string;
}
