export function unidadeControleOpt(): Array<{ value: string; label: string }> {
  return [
    { value: 'UN', label: 'UN' },
    { value: 'KG', label: 'KG' },
    { value: 'LITROS', label: 'Litros' },
    { value: 'CAIXAS', label: 'Caixas' },
    { value: 'REAIS', label: 'Reais' },
  ];
}

export function produtoCategoriaOpt(): Array<{ value: string; label: string }> {
  return [
    { value: 'FINANCEIRO', label: 'Financeiro' },
    { value: 'VESTUARIO', label: 'Vestuário' },
    { value: 'ALIMENTO', label: 'Alimento' },
    { value: 'HIGIENE', label: 'Higiene' },
    { value: 'LIMPEZA', label: 'Limpeza' },
    { value: 'MEDICAMENTO', label: 'Medicamento' },
    { value: 'OUTRO', label: 'Outros' },
  ];
}
