import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { NewUserComponent } from './components/new-user/new-user.component';
import { CatalogComponent } from './components/catalog/catalog.component';
import { CustomerAuthenticationService } from './services/customerAuthService/customer-authentication.service';
import { UserAuthenticationService } from './services/userAuthService/user-authentication.service';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
const routes: Routes = [
  { path: "new-user", component: NewUserComponent },
  { path: "login", component: LoginComponent },
  { path: "catalog", component: CatalogComponent },
  { path: "admin-dashboard", component: AdminDashboardComponent},
  { path: "detail/:id", component: ProductDetailsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
