import { Component } from '@angular/core';
import { User, UserService } from 'src/app/services/userService/user.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent {
  constructor (private userService: UserService){}

  user = this.userService.getLoggedIn();

  ngOnInit():void{
    console.log(this.user)
  }
}
