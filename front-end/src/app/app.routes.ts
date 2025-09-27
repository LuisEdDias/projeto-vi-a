import { Routes } from '@angular/router';
import { ProdutoListComponent } from './features/produto/produto-list/produto-list.component';
import { ProdutoCreateComponent } from './features/produto/produto-create/produto-create.component';

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
        component: ProdutoCreateComponent
      }
    ],
  },
];
