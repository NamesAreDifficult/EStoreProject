import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Beef, BeefService } from 'src/app/services/beefService/beef.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})

export class AdminDashboardComponent {
  beefService: BeefService;
  beef$!: Observable<Beef[]>;
  adminAlert: String = ""

  constructor(beefService: BeefService) {
    this.beefService = beefService;
  }

  ngOnInit(){
    // Initiates inventory display
    this.getInventory();
  }

  getInventory = async(): Promise<void> => {
    this.beef$ = this.beefService.getAllBeef();
  }
  
  createObserver = {
    next: (beef: Beef) => {
      this.beefService.addBeef(beef);
      this.getInventory();
    },
    error: (err: Error) => (this.createStatus(Number(err.message)))
  }

  deleteObserver = {
    next: (beef: Beef) => {
      this.beefService.deleteBeef(beef.id!);
      this.getInventory();
    },
    error: (err: Error) => (this.deleteStatus(Number(err.message)))
  }

  private createStatus(code: number) {
    // Conflict error
    if (code == 409) {
      this.adminAlert = "Item already exists. Please edit quantity to change amount."
    // Creating item with invalid fields
    } else if (code == 400) {
      this.adminAlert = "Items cannot have negative weight or price."
    // Internal server error
    } else if (code == 500) {
      this.adminAlert = "Internal server error"
    }
  }

  private deleteStatus(code: number) {
    // Item not found error
    if (code == 404) {
      this.adminAlert = "Item not found."
    // Internal server error
    } else if (code == 500) {
      this.adminAlert = "Internal server error"
    }
  }

  validateCreate(cut: string, weight: number, grade: string, price: number): boolean {
    var cutRegexp = new RegExp('^[a-zA-Z0-9 ]{1,32}$');
    var gradeRegexp = new RegExp('^[a-zA-Z0-9 ]{1,32}$');
    if ((cutRegexp.test(cut) && cut.trim()) && (gradeRegexp.test(grade) && grade.trim())){
      return true;
    }
    this.adminAlert = "Cut and grade must be alphanumeric with least one non-whitespace character, and have a length of 1-32 characters."
    return false;
  }

  create(cut: string, weight: number, grade: string, price: number) {
    if (this.validateCreate(cut, weight, grade, price)){
      var beef: Beef = {
        cut: cut.trim(),
        weight: weight,
        grade: grade.trim(),
        price: price
      }
    this.beefService.addBeef(beef).subscribe(this.createObserver);
    this.adminAlert = "Product created."
    window.location.reload();
    }
  }

  delete(id: number) {
    this.beefService.deleteBeef(id).subscribe(this.deleteObserver);
    this.adminAlert = "Product deleted."
    window.location.reload();
  }
}
