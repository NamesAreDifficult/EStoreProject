import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { LoginUser, User, UserService } from '../user.service';

@Component({
  selector: 'app-new-user',
  templateUrl: './new-user.component.html',
  styleUrls: ['./new-user.component.css']
})
export class NewUserComponent implements OnInit {
  // Property to store an instance of the UserService class
  userService: UserService;

  userAlert: String = ""

  Observer = {
    next: (user: User) => {
      this.userAlert = `Welcome ${user.username}!`;
      this.userService.signUserIn(user);
    },
    error: (err: Error) => (this.catchStatusCode(Number(err.message)))
  }

  // Catches status codes from the backend
  private catchStatusCode(code: number) {
    // Conflict error
    if (code == 409) {
      this.userAlert = "Username is already taken, please enter another"
    } else if (code == 500) {
      this.userAlert = "Internal server error"
    }

  }

  // Constructor for the LandingComponent that takes an instance of UserService
  constructor(userService: UserService) {

    // Initializing the userService property with the provided instance of UserService
    this.userService = userService;
  }

  ngOnInit() {
    var loggedIn: User | null = this.userService.getLoggedIn();
    if (loggedIn != null) {
      this.userAlert = `You are already logged in ${loggedIn.username}`
    }
  }

  // Method that takes a username string parameter and returns a User object
  submit(username: string) {
    // Creating and returning a User object with the username property set to the provided value
    var newUser: LoginUser = {
      username: username
    }

    this.userService.createCustomer(newUser)
      .subscribe(
        this.Observer
      );
  }

}
