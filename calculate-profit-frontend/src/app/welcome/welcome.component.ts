import { Component } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-welcome',
  template: `
    <div class="d-flex justify-content-center align-items-center w-100 h-100 placeholder-container">
      <div class="text-muted text-center">
        <h4 class="mb-2">Welcome</h4>
        <p>Please select <strong>Calculate Profit</strong> from the menu to begin.</p>
      </div>
    </div>
  `,
  styles: [
    `
      .placeholder-container {
        min-height: 100vh;
      }
    `
  ]
})
export class WelcomeComponent {}
