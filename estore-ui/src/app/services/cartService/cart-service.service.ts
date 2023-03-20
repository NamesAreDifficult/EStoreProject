import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap, catchError, throwError } from 'rxjs';
import { Beef, BeefService } from '../beefService/beef.service';
import { LoggingService } from '../loggingService/logging.service';
import { User, UserService } from '../userService/user.service';


export interface CartBeef {
  id: number;
  weight: number
}



@Injectable({
  providedIn: 'root'
})
export class CartServiceService {

  shoppingUrl: string = "http://localhost:8080/shopping"

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient, private logger: LoggingService, private userService: UserService) { }

  public getCart(): Observable<Beef[]> {

    var user: User | null = this.userService.getLoggedIn();

    var username: String;

    if (user != null) {
      username = user.username;
    } else {
      username = "";
    }

    return this.http.get<Beef[]>(this.shoppingUrl + "/" + username, this.httpOptions).pipe(
      tap(_ => this.logger.add(`Got Cart for customer: ${username}`)),
      catchError(err => {
        this.logger.handleError<any>('getCart')
        return throwError((() => new Error(err.status)));

      })
    );
  }
}
