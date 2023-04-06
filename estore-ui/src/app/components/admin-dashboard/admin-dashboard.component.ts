import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Beef, BeefService } from 'src/app/services/beefService/beef.service';
import { User, UserService } from 'src/app/services/userService/user.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})

export class AdminDashboardComponent {
  // Stores instance of BeefService class
  beefService: BeefService
  // Observable used to update inventory on page
  beef$!: Observable<Beef[]>
  // Alert for displaying success/error messages
  adminAlert: String = ""
  userService: UserService
  user?: User | null

  // Constructor for Admin Dashboard that takes an instance of BeefService and AdminAuthenticationService
  constructor(beefService: BeefService, userService: UserService) {
    this.beefService = beefService
    this.userService = userService;
  }

  // Initializes the inventory
  ngOnInit() {
    this.beef$ = this.beefService.getAllBeef()
    this.userService.userNotifier.subscribe(currentUser => {
      this.user = currentUser;
      if (currentUser == null) {
        location.reload()
      }
    });
  }

  // Observer for handling creation of new beef 
  createObserver = {
    next: (beef: Beef) => {
      this.beefService.addBeef(beef)
    },
    error: (err: Error) => (this.createStatus(Number(err.message))),
    complete: () => this.beef$ = this.beefService.getAllBeef(),
  }

  // Observer for handling deletion of beef 
  deleteObserver = {
    next: (beef: Beef) => {
      this.beefService.deleteBeef(beef.id!)
    },
    error: (err: Error) => (this.deleteStatus(Number(err.message))),
    complete: () => this.beef$ = this.beefService.getAllBeef(),
  }

  // Observer for handling updating of beef fields
  updateObserver = {
    next: (beef: Beef) => {
      this.beefService.updateBeef(beef)
    },
    error: (err: Error) => (this.updateStatus(Number(err.message))),
    complete: () => this.beef$ = this.beefService.getAllBeef()
  }

  // Catches error code for create method and displays text
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

  // Catches error code for delete method and displays text
  private deleteStatus(code: number) {
    // Item not found error
    if (code == 404) {
      this.adminAlert = "Item not found."
      // Internal server error
    } else if (code == 500) {
      this.adminAlert = "Internal server error"
    }
  }

  // Catches error code for update method and displays text
  private updateStatus(code: number) {
    // Item not found error
    if (code == 404) {
      this.adminAlert = "Item not found."
      // Internal server error
    } else if (code == 500) {
      this.adminAlert = "Internal server error"
    } else if (code == 400) {
      this.adminAlert = "Items cannot have negative weight or price."
    }
  }

  // Validates beef if cut and grade are alphanumeric with 1-32 characters and at least one non-whitespace character
  validateCreate(cut: string, grade: string): boolean {
    var cutRegexp = new RegExp('^[a-zA-Z0-9 ]{1,32}$')
    var gradeRegexp = new RegExp('^[a-zA-Z0-9 ]{1,32}$')
    if ((cutRegexp.test(cut) && cut.trim()) && (gradeRegexp.test(grade) && grade.trim())) {
      return true
    }
    this.adminAlert = "Cut and grade must be alphanumeric with least one non-whitespace character, and have a length of 1-32 characters."
    return false
  }

  // Validates beef if price and weight are non-negative numbers
  validateUpdate(beef: Beef, weight: number, price: number): boolean {
    if (beef.weight + weight >= 0 && price >= 0) {
      return true
    }
    this.adminAlert = "Items cannot have negative weight or price."
    return false
  }

  // Adds new beef object to inventory if the fields are valid
  create(cut: string, weight: number, grade: string, price: number, imageUrl: string) {
    if (this.validateCreate(cut, grade)) {
      if (Number(price.toFixed(2)) > 0 && Number(weight.toFixed(2)) > 0) {


        var beef: Beef = {
          cut: cut.trim(),
          weight: Number(weight.toFixed(2)),
          grade: grade.trim(),
          price: Number(price.toFixed(2)),
          imageUrl: imageUrl
        }
        this.beefService.addBeef(beef).subscribe(this.createObserver)
        this.adminAlert = "Product created."
      }
      else {
        this.adminAlert = "Items cannot have negative weight or price."
      }
    }
  }

  // Deletes beef object from the inventory
  delete(id: number) {
    this.beefService.deleteBeef(id).subscribe(this.deleteObserver);
    this.adminAlert = "Product deleted."
  }

  // Updates price and weight of beef objects if the fields are valid
  update(beef: Beef, weight: number, price: number, imageUrl: string) {
    if (this.validateUpdate(beef, weight, price)) {
      beef.weight = Number(weight.toFixed(2))
      beef.price = Number(price.toFixed(2))
      beef.imageUrl = imageUrl
      this.beefService.updateBeef(beef).subscribe(this.updateObserver);
      this.adminAlert = "Product updated."
    }
  }
}