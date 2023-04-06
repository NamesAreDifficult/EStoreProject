import { Component, OnInit } from '@angular/core';
import { LoginUser, User, UserService } from '../../services/userService/user.service';

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
      this.userService.signUserIn(user);
      this.checkLogin()
    },
    error: (err: Error) => (this.catchStatusCode(Number(err.message)))

  }

  // Catches status codes from the backend
  private catchStatusCode(code: number) {

    // Conflict error
    if (code == 404) {
      this.warning = "User not found, try again"
    } else if (code == 500) {
      this.warning = "Internal server error"
    }
  }

  private checkLogin() {
    var loggedIn: User | null = this.userService.getLoggedIn()
    if (loggedIn != null) {
      this.warning = `Logging in as: ${loggedIn.username}`
      location.reload();
    }
  }


  constructor(userService: UserService) {

    // Initializing the userService property with the provided instance of UserService
    this.userService = userService;
  }

  ngOnInit() {
    var loggedIn: User | null = this.userService.getLoggedIn();
    if (loggedIn != null) {
      this.warning = `You are already logged in ${loggedIn.username}`
    }
  }



  public submit(username: string, password: string) {

    if (username == "") {
      this.warning = "Username is required"
      return;
    } else if(password == ""){
      this.warning = "Password is required"
      return;
    }

    // Creating and returning a User object with the username property set to the provided value
    var user: LoginUser = {
      username: username,
      password: password
    }

    if (user) {
      this.userService.loginUser(user)
        .subscribe({
          next: (user) => {
            this.Observer.next(user)
          },
          error: (err) => {
            this.warning = "Username or password incorrect"
          }});
    }
  }

}
