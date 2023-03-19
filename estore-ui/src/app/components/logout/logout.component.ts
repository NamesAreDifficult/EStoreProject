import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/userService/user.service';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent {


  constructor(private userService: UserService, private router: Router) { }

  logout() {
    this.userService.logout();

    // Sends the user back to the front page
    this.router.navigate(['/']);
  }

}
