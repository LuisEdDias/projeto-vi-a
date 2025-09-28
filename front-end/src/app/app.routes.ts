import { Routes } from '@angular/router';
import { ProdutoListComponent } from './features/produto/produto-list/produto-list.component';
import { ProdutoCreateComponent } from './features/produto/produto-create/produto-create.component';
import { DoadorCreateComponent } from './features/doador/doador-create/doador-create.component';
import { DoadorListComponent } from './features/doador/doador-list/doador-list.component';
import { DoacaoCreateComponent } from './features/doacao/doacao-create/doacao-create.component';
import { DoacaoListComponent } from './features/doacao/doacao-list/doacao-list.component';

export const routes: Routes = [
  {
    path: 'produto',
    children: [
      {
        path: '',
        component: ProdutoListComponent,
      },
      {
        path: 'cadastro',
        component: ProdutoCreateComponent,
      },
    ],
  },
  {
    path: 'doador',
    children: [
      {
        path: '',
        component: DoadorListComponent,
      },
      {
        path: 'cadastro',
        component: DoadorCreateComponent,
      },
    ],
  },
  {
    path: 'doacao',
    children: [
      {
        path: '',
        component: DoacaoListComponent
      },
      {
        path: 'cadastro',
        component: DoacaoCreateComponent,
      },
    ],
  },
];
