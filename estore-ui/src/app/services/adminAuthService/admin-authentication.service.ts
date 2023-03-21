import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { Router } from '@angular/router';
import { LoggingService } from '../loggingService/logging.service';
import { UserService, User } from '../userService/user.service';


/*
* Class used for designating admin only pages
*/
@Injectable()
export class AdminAuthenticationService implements CanActivate {
  canActivate() {
    var currentUser: User | null = this.userService.getLoggedIn()
    if (currentUser != null) {
      //  User is an admin
      if (currentUser.admin) {
        return true;
      }
      else {
        this.router.navigate(['catalog'])
        return false;
      }
    }
    this.router.navigate(['login'])
    this.logger.add('Admin Auth#canActivate called');
    return false;
  }

  //Constructor 
  constructor(private router: Router, private userService: UserService, private logger: LoggingService) { }
}
