import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { NewUserComponent } from './components/new-user/new-user.component';
import { CatalogComponent } from './components/catalog/catalog.component';
import { CustomerAuthenticationService } from './services/customerAuthService/customer-authentication.service';
import { UserAuthenticationService } from './services/userAuthService/user-authentication.service';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { AdminAuthenticationService } from './services/adminAuthService/admin-authentication.service';
const routes: Routes = [
  { path: "new-user", component: NewUserComponent, canActivate: [UserAuthenticationService] },
  { path: "login", component: LoginComponent, canActivate: [UserAuthenticationService] },
  { path: "catalog", component: CatalogComponent },
  { path: "home-page", component: HomePageComponent },
  { path: "admin-dashboard", component: AdminDashboardComponent, canActivate: [AdminAuthenticationService] },
  { path: '', redirectTo: '/home-page', pathMatch: 'full' },
  { path: "product/:id", component: ProductDetailsComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
