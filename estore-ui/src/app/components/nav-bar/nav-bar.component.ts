import { Component } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { User, UserService } from 'src/app/services/userService/user.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent {
  constructor (private userService: UserService){}

  user = this.userService.getLoggedIn();

  //Watches for changes in the user to update the navbar
  ngOnInit() {
    this.userService.userNotifier.subscribe(currentUser => {
      this.user = currentUser;
    });
  }


}
