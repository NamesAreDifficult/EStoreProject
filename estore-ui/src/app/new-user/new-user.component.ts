import { Component, OnInit } from '@angular/core';
import { User, UserService } from '../user.service';

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
    },
    error: (err: Error) => (console.log(err))

  }

  // Constructor for the LandingComponent that takes an instance of UserService
  constructor(userService: UserService) {

    // Initializing the userService property with the provided instance of UserService
    this.userService = userService;
  }

  ngOnInit() {
    var loggedIn: User = this.userService.getLoggedIn();
    if (loggedIn != null) {
      this.userAlert = `You are already logged in ${loggedIn.username}`
    }
  }

  // Method that takes a username string parameter and returns a User object
  submit(username: string): User {
    // Creating and returning a User object with the username property set to the provided value
    var newUser: User = { username: username }

    if (newUser) {
      this.userService.createCustomer(newUser)
        .subscribe(
          this.Observer
        );
    }

    return newUser
  }

}
