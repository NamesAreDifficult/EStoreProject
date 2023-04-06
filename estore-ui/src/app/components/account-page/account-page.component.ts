import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/userService/user.service';
import { User } from 'src/app/services/userService/user.service';

@Component({
  selector: 'app-account-page',
  templateUrl: './account-page.component.html',
  styleUrls: ['./account-page.component.css']
})
export class AccountPageComponent {
  userService: UserService;
  user: User | null;

  constructor(userService: UserService){
    this.userService = userService;
    this.user = userService.getLoggedIn();
  };

  ngOnInit(){
    this.userService.userNotifier.subscribe(currentUser => {
      this.user = currentUser;
      if(currentUser == null){
        location.reload()
      }
    });
  }
}
