import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { Router } from '@angular/router';
import { LoggingService } from '../loggingService/logging.service';
import { UserService, User } from '../userService/user.service';


/*
* Class used for designating a page as only for users not logged in
*/
@Injectable()
export class CustomerAuthenticationService implements CanActivate {
  canActivate() {
    var currentUser: User | null = this.userService.getLoggedIn()

    if (currentUser == null) {
      return true;
    }


    else {
      // User is not logged in, go to new-user
      this.router.navigate([''])
    }

    this.logger.add('UserAuth#canActivate called');
    return true;
  }
  //Constructor 
  constructor(private router: Router, private userService: UserService, private logger: LoggingService) { }
}
