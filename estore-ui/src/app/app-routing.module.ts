import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { NewUserComponent } from './components/new-user/new-user.component';
import { CatalogComponent } from './components/catalog/catalog.component';
import { HomePageComponent } from './components/home-page/home-page.component';
const routes: Routes = [
  { path: "new-user", component: NewUserComponent },
  { path: "login", component: LoginComponent },
  { path: "catalog", component: CatalogComponent },
  { path: "home page", component: HomePageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
