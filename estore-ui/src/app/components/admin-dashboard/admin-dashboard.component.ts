import { Component } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { Beef, BeefService } from 'src/app/services/beefService/beef.service';

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

  createObserver = {
    next: (beef: Beef) => {
      this.beefService.addBeef(beef);
    },
    error: (err: Error) => (this.catchStatusCode(Number(err.message)))
  }

  private catchStatusCode(code: number) {
    // Conflict error
    if (code == 409) {
      this.adminAlert = "Item already exists. Please edit quantity to change amount."
    } else if (code == 400) {
      this.adminAlert = "Invalid request."
    } else if (code == 200) {
      this.adminAlert = ""
    } else if (code == 404) {
      this.adminAlert = "Item not found."
    } else if (code == 500) {
      this.adminAlert = "Internal server error"
    }

  }

  create(cut: string, weight: number, grade: string, price: number){
    var beef: Beef = {
      cut: cut,
      weight: weight,
      grade: grade,
      price: price
    }
    this.beefService.addBeef(beef).subscribe(this.createObserver);
  }
}
