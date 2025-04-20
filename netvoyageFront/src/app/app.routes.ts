import { Routes } from '@angular/router';
import { InscEntrepriseComponent } from './insc-entreprise/insc-entreprise.component';
import { LoginEntrepriseComponent } from './login-entreprise/login-entreprise.component';

export const routes: Routes = [
{
    path : 'insc-entreprise' , component : InscEntrepriseComponent 
},
{
    path : 'login-entreprise' , component : LoginEntrepriseComponent

}
];
