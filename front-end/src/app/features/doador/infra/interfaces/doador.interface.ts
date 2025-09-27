export interface DoadorResponseDTO {
  id: number;
  nome: string;
  email: string;
  docIdentificacao: string;
}

export interface DoadorCreateDTO {
  nome: string;
  email: string;
  docIdentificacao: string;
}
