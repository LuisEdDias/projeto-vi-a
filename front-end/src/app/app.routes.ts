import { Routes } from '@angular/router';
import { ProdutoListComponent } from './features/produto/produto-list/produto-list.component';

export const routes: Routes = [
  {
    path: 'produto',
    children: [
      {
        path: '',
        component: ProdutoListComponent,
      },
    ],
  },
];
