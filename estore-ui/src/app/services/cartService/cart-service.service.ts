import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap, catchError, throwError } from 'rxjs';
import { BeefService } from '../beefService/beef.service';
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

  constructor(private http: HttpClient, private logger: LoggingService) { }

  public getCart(username: String): Observable<any> {

    return this.http.get<CartBeef[]>(this.shoppingUrl + "/" + username, this.httpOptions).pipe(
      tap(_ => this.logger.add(`Got Cart for customer: ${username}`)),
      catchError(err => {
        this.logger.handleError<any>('createCustomer')
        return throwError((() => new Error(err.status)));

      })
    );
  }
}
