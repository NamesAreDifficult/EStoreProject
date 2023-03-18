import { Component } from '@angular/core';
import { BeefService } from 'src/app/services/beefService/beef.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent {
  beefService: BeefService;
  adminAlert: String = ""

  constructor(beefService: BeefService) {
    this.beefService = beefService;
  }

  private catchStatusCode(code: number) {
    // Conflict error
    if (code == 409) {
      this.adminAlert = "Item already exists. Please edit quantity to change amount."
    } else if (code == 400) {
      this.adminAlert = "Invalid attributes. If removing an item, please click delete instead."
    } else if (code == 200) {
      this.adminAlert = ""
    } else if (code == 404) {
      this.adminAlert = "Item not found."
    } else if (code == 500) {
      this.adminAlert = "Internal server error"
    }

  }
}
