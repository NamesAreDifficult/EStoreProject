import { Component } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
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
  private beefSubject = new BehaviorSubject(this.beef$)

  constructor(beefService: BeefService) {
    this.beefService = beefService;
  }

  ngOnInit(): void{
    // Initiates inventory display
    this.displayInventory();
  }

  displayInventory = async(): Promise<void> => {
    this.beef$ = this.beefSubject.pipe(
      debounceTime(1),
      distinctUntilChanged(),
      switchMap(() => this.beefService.getAllBeef()),
    );
  }
  
  createObserver = {
    next: (beef: Beef) => {
      this.beefService.addBeef(beef);
    },
    error: (err: Error) => (this.createStatus(Number(err.message)))
  }

  private createStatus(code: number) {
    // Conflict error
    if (code == 409) {
      this.adminAlert = "Item already exists. Please edit quantity to change amount."
    // Creating item with missing fields
    } else if (code == 400) {
      this.adminAlert = "Items cannot have negative weight or price."
    // Reset message if good
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
    }
    this.displayInventory()
  }
}
