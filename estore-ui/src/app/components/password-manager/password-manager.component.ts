import { Component } from '@angular/core';
import { UserService } from 'src/app/services/userService/user.service';

@Component({
  selector: 'app-password-manager',
  templateUrl: './password-manager.component.html',
  styleUrls: ['./password-manager.component.css']
})
export class PasswordManagerComponent {
  userService: UserService;
  warning: string = "";

  constructor(userService: UserService){
    this.userService = userService;
  }

  resetPassword(oldPassword: string, newPassword: string, confirmPassword: string){
    this.warning = ""
    if(newPassword != confirmPassword){
      this.warning = "New password and confirmation do not match"
    }
    this.userService.updatePassword(oldPassword, newPassword)
  }
}
