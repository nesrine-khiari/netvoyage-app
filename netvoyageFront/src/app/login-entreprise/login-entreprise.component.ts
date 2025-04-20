import { Component } from '@angular/core';

@Component({
  selector: 'app-login-entreprise',
  imports: [],
  templateUrl: './login-entreprise.component.html',
  styleUrl: './login-entreprise.component.css'
})
export class LoginEntrepriseComponent {
  credentials = {
    email: '',
    password: ''
  };

  onSubmit() {
    // Ici, vous implémenterez la logique de connexion
    console.log('Login attempt with:', this.credentials);
    // Appel à un service d'authentification
  }


}
