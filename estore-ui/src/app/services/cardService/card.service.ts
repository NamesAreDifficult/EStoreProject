import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, tap, of, throwError, BehaviorSubject } from 'rxjs';
import { LoggingService } from '../loggingService/logging.service';
import { User, UserService } from '../userService/user.service';

export interface CreditCard {
  number: string;
  expiration: string;
  cvv: string;
}

@Injectable({
  providedIn: 'root'
})
export class CardService {

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  private userUrl = 'http://localhost:8080/user'
  // Constructor
  constructor(private http: HttpClient, private logger: LoggingService, private userService: UserService) { }

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

  // Gets a customer's credit cards from the backend
  getCards(): Observable<CreditCard[]> {
    const url = this.userUrl + "/cards/" + this.getUsername()
    return this.http.get<CreditCard[]>(url, this.httpOptions).pipe(
      tap(_ => this.logger.add(`Got cards from: ${this.getUsername()}`)),
      catchError(err => {
        this.logger.handleError<any>('getCards')
        return throwError((() => new Error(err.status)));
      })
    );
  }

  // Adds a customers credit card to the backend
  addCard(card: CreditCard): Observable<CreditCard> {
    const url = this.userUrl + "/" + this.getUsername()
    return this.http.post<CreditCard>(url, card, this.httpOptions).pipe(
      tap(_ => this.logger.add(`Added card to: ${this.getUsername()}`)),
      catchError(err => {
        this.logger.handleError<any>('addCard')
        return throwError((() => new Error(err.status)));
      })
    );
  }

  // Removes a customer's credit card from the backend
  removeCard(cardnumber: string): Observable<any> {
    const url = this.userUrl + "/" + this.getUsername() + "/" + cardnumber
    return this.http.delete(url, this.httpOptions).pipe(
      tap(_ => this.logger.add(`Got cards from: ${this.getUsername()}`)),
      catchError(err => {
        this.logger.handleError<any>('getCards')
        return throwError((() => new Error(err.status)));
      })
    );
  }
}
