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
    // Todo Implement
    return true;
  }
  //Constructor 
  constructor(private router: Router, private userService: UserService, private logger: LoggingService) { }
}
