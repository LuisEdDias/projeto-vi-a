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

export interface DoadorUpdateDTO {
  nome: string;
  email: string;
}

export interface DoadorWithDoacoesResponseDTO extends DoadorResponseDTO {
  doacoes: DoacaoDTO[];
}

interface DoacaoDTO {
  id: number;
  descricao: string;
  data: string
}