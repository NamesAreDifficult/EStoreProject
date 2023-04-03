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

    return this.http.get<Beef[]>(this.shoppingUrl + "/" + this.getUsername(), this.httpOptions).pipe(
      tap(_ => this.logger.add(`Got Cart for customer: ${this.getUsername()}`)),
      catchError(err => {
        this.logger.handleError<any>('getCart')
        return throwError((() => new Error(err.status)));

      })
    );
  }

  private getUsername(): String {
    var user: User | null = this.userService.getLoggedIn();

    var username: String;
    if (user != null) {
      username = user.username;
    } else {
      username = "";
    }
    return username;
  }

  public removeFromCart(id: number): Observable<any> {
    const url = this.shoppingUrl + "/" + this.getUsername() + "/" + String(id)
    return this.http.delete(url, this.httpOptions).pipe(
      tap(_ => this.logger.add(`Removed ${id} from ${this.getUsername()}'s shopping cart`)),
      catchError(err => {
        this.logger.handleError<any>('removeFromCart')
        return throwError((() => new Error(err.status)));
      }))
  }

  public addToCart(cartBeef: CartBeef): Observable<any> {
    const url = this.shoppingUrl + "/" + this.getUsername()
    return this.http.post<CartBeef>(url, cartBeef, this.httpOptions).pipe(
      tap(_ => this.logger.add(`Added ${cartBeef.id} to ${this.getUsername()}`)),
      catchError(err => {
        this.logger.handleError<any>('addToCart')
        return throwError((() => new Error(err.status)));
      })
    );

  }

  public checkout(): Observable<any> {
    const url = this.shoppingUrl + "/checkout/" + this.getUsername()
    return this.http.put<CartBeef>(url, this.httpOptions).pipe(
      tap(_ => this.logger.add(`Checked out ${this.getUsername()}`)),
      catchError(err => {
        this.logger.handleError<any>('checkout')
        return throwError((() => new Error(err.status)));
      })
    );

  }
}
