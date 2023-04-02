import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, tap, of, throwError, BehaviorSubject } from 'rxjs';
import { LoggingService } from '../loggingService/logging.service';

export interface User {
  username: string;
  admin: boolean;
}

export interface NewUser{
  username: string;
  admin: boolean;
  password: string;
}

export interface LoginUser {
  username: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };
  private userUrl = 'http://localhost:8080/user'
  // Constructor
  constructor(private http: HttpClient, private logger: LoggingService) { }

  userNotifier = new EventEmitter();

  // Gets a customer from the backend
  createCustomer(customer: NewUser): Observable<any> {
    return this.http.post<User>(this.userUrl + "/customer", customer, this.httpOptions).pipe(
      tap(_ => this.logger.add(`Created customer: ${customer.username} ${customer.password} ${customer.admin}`)),
      catchError(err => {
        this.logger.handleError<any>('createCustomer')
        return throwError((() => new Error(err.status)));
      })
    );
  }

  //  Logs in a user using the backend
  loginUser(user: LoginUser): Observable<any> {
    return this.http.get<User>(this.userUrl + `/${user.username}`, this.httpOptions).pipe(
      tap(_ => {
        this.logger.add(`Logged in user: ${user.username}`);
        this.userNotifier.emit(user);
      }),
      catchError(
        err => {
          this.logger.handleError<any>('loginUser')
          return throwError((() => new Error(err.status)));
        })
    );
  }


  // Get signed in user
  public getLoggedIn(): User | null {
    var username = localStorage.getItem("user")

    // Converts string to boolean
    var admin = (localStorage.getItem("admin") === 'true')
    if (username) {
      var user: User = {
        username: username,
        admin: admin
      }
      return user;
    }
    return null;
  }

  // Signs user in
  public signUserIn(user: User) {
    this.logger.add(`signUserIn: ${user.username}`)
    localStorage.setItem("user", user.username);
    localStorage.setItem("admin", String(user.admin));
  }

  public logout() {
    var user: User | null = this.getLoggedIn();

    if (user != null) {
      this.userNotifier.emit(null);
      this.logger.add(`logout: ${user.username}`)
      localStorage.clear();
    }
  }

}
