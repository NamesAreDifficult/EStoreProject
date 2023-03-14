import { Component, OnInit } from '@angular/core';
import { User, UserService } from '../user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  userService: UserService;
  warning: string = "";

  Observer = {
    next: (user: User) => {
      this.userService.setLoggedInUser(user);
      this.checkLogin()
    },
    error: (err: Error) => (console.log(err))

  }

  checkLogin() {
    var loggedIn: User = this.userService.getLoggedIn()
    if (loggedIn != null) {
      this.warning = `You are logged in as ${loggedIn.username} admin: ${loggedIn.admin}`
    } else {
      this.warning = "You are not logged in"
    }
  }


  constructor(userService: UserService) {

    // Initializing the userService property with the provided instance of UserService
    this.userService = userService;
  }

  ngOnInit() {
    var loggedIn: User = this.userService.getLoggedIn();
    if (loggedIn != null) {
      this.warning = `You are already logged in ${loggedIn.username}`
    } else {
      this.warning = "You are not currently logged in"
    }
  }



  submit(username: string): User {
    // Creating and returning a User object with the username property set to the provided value
    var user: User = { username: username }

    if (user) {
      this.userService.loginUser(user)
        .subscribe(this.Observer);
    }
    return user
  }

}
