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
  private logout_button = document.getElementById("logout")

  logout() {
    this.userService.logout();
    this.logout_button = document.getElementById("logout")
    if (this.logout_button != null) {
      this.logout_button.style.display = "none"
    }
    // Sends the user back to the front page
    this.router.navigate(['/login']);
  }

}
