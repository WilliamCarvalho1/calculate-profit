import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./welcome/welcome.component').then(m => m.WelcomeComponent)
  },
  {
    path: 'calculate-profit',
    loadComponent: () => import('./calculate-profit/calculate-profit.component').then(m => m.CalculateProfitComponent)
  },
  { path: '**', redirectTo: '' }
];
