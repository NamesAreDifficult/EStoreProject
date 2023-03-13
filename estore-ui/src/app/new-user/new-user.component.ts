import { Component } from '@angular/core';
import { User, UserService } from '../user.service';

@Component({
  selector: 'app-new-user',
  templateUrl: './new-user.component.html',
  styleUrls: ['./new-user.component.css']
})
export class NewUserComponent {
  // Property to store an instance of the UserService class
  userService: UserService;

  userAlert: String = ""

  // Constructor for the LandingComponent that takes an instance of UserService
  constructor(userService: UserService) {

    // Initializing the userService property with the provided instance of UserService
    this.userService = userService;
  }

  // Method that takes a username string parameter and returns a User object
  submit(username: string): User {
    // Creating and returning a User object with the username property set to the provided value
    var newUser: User = { username: username }

    if (newUser) {
      this.userService.createCustomer(newUser)
        .subscribe();
    }

    this.userAlert = `Welcome ${newUser.username}!`
    return newUser
  }

}
