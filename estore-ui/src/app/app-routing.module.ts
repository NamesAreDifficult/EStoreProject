import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { NewUserComponent } from './components/new-user/new-user.component';
import { CatalogComponent } from './components/catalog/catalog.component';
import { CustomerAuthenticationService } from './services/customerAuthService/customer-authentication.service';
import { UserAuthenticationService } from './services/userAuthService/user-authentication.service';
import { HomePageComponent } from './components/home-page/home-page.component';

const routes: Routes = [
  { path: "new-user", component: NewUserComponent, canActivate: [UserAuthenticationService] },
  { path: "login", component: LoginComponent, canActivate: [UserAuthenticationService] },
  { path: "catalog", component: CatalogComponent, canActivate: [CustomerAuthenticationService] },
  { path: "home-page", component: HomePageComponent},
  { path: '', redirectTo: '/home-page', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
