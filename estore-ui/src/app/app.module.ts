import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NewUserComponent } from './components/new-user/new-user.component';
import { HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './components/login/login.component';
import { CatalogComponent } from './components/catalog/catalog.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { UserAuthenticationService } from './services/userAuthService/user-authentication.service';
import { CustomerAuthenticationService } from './services/customerAuthService/customer-authentication.service';
import { AdminAuthenticationService } from './services/adminAuthService/admin-authentication.service';
import { LogoutComponent } from './components/logout/logout.component';

@NgModule({
  declarations: [
    AppComponent,
    NewUserComponent,
    LoginComponent,
    CatalogComponent,
    AdminDashboardComponent,
    LogoutComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [UserAuthenticationService, CustomerAuthenticationService, AdminAuthenticationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
