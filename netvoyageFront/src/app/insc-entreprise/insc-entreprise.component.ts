import { Component } from '@angular/core';

@Component({
  selector: 'app-insc-entreprise',
  imports: [],
  templateUrl: './insc-entreprise.component.html',
  styleUrl: './insc-entreprise.component.css'
})
export class InscEntrepriseComponent {
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
