import { Injectable } from '@angular/core';


export interface User {
  username: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userUrl = 'http://localhost:8080/User'
  constructor() { }

  createUser(user: User) {

  }

}
