import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: 'calculate-profit',
    loadComponent: () => import('./calculate-profit/calculate-profit.component').then(m => m.CalculateProfitComponent)
  },
  {path: '', redirectTo: '/calculate-profit', pathMatch: 'full'}
];
