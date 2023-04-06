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
    console.log(newPassword, confirmPassword)
    if(!new RegExp('^[a-zA-Z0-9!@#$%^&*()-_+=?]{8,26}$').test(newPassword)){
      this.warning = "Passwords must be between 8-26 alphanumeric and special characters. Allowed special Characters: !@#$%^&*()-_+=?";
      return;
    }
    if(newPassword != confirmPassword){
      this.warning = "New password and confirmation do not match"
      return
    }
    var result = this.userService.updatePassword(oldPassword, newPassword)
    console.log(result)
    result.subscribe(num =>{
      switch(num){
        case -2:
          this.warning = "Internal server Error"
          break
        case -1:
          this.warning = "User not found"
          break
        case 200:
          this.warning = "Password updated"
          break
        case 401:
          this.warning = "Wrong password provided"
          break
        case 500:
          this.warning = "Internal server error"
          break
        default:
          this.warning = "Unknown error occured"
      }
    })
  }
}
