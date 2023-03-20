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
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { FormsModule } from '@angular/forms';
import { MessagesComponent } from '/Users/aj/SWEN/Project/team-project-2225-swen-261-06-c-cowrelatedpun/estore-ui/src/app/components/messages/messages.component';

@NgModule({
  declarations: [
    AppComponent,
    NewUserComponent,
    LoginComponent,
    CatalogComponent,
    AdminDashboardComponent,
    LogoutComponent,
    ProductDetailsComponent,
    MessagesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [UserAuthenticationService, CustomerAuthenticationService, AdminAuthenticationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
