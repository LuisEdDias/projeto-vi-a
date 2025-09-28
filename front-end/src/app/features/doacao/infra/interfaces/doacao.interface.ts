import { DoadorResponseDTO } from '../../../doador/infra/interfaces/doador.interface';

export interface DoacaoResponseDTO {
  doacaoId: number;
  descricao: string;
  data: string;
  doadorId: number;
  doadorNome: string;
}

export interface ProdutoQuantidadeDTO {
  produtoId: number;
  quantidade: number;
}

export interface DoacaoCreateDTO {
  descricao: string;
  doadorId: number;
  data: string;
  itens: ProdutoQuantidadeDTO[];
}

interface DoacaoProdutoDTO {
  id: number;
  produtoId: number;
  produtoNome: string;
  produtoCategoria: string;
  produtoUnidadeControle: string;
  quantidade: number;
}

export interface DoacaoWithItensResponseDTO {
  doacaoId: number;
  descricao: string;
  data: string;
  doador: DoadorResponseDTO;
  itens: DoacaoProdutoDTO[];
}
