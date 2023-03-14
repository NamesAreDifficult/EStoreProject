import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, tap, of } from 'rxjs';

export interface User {
  username: string;
  admin?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  private userUrl = 'http://localhost:8080/user'

  // Constructor
  constructor(private http: HttpClient) { }


  // Gets a customer from the backend
  createCustomer(customer: User): Observable<any> {
    return this.http.post(this.userUrl + "/customer", customer, this.httpOptions).pipe(
      tap(_ => this.log(`Created customer: ${customer.username}`)),
      catchError(this.handleError<any>('createCustomer'))
    );
  }

  //  Logs in a user using the backend
  loginUser(user: User): Observable<any> {
    return this.http.get(this.userUrl + `/${user.username}`, this.httpOptions).pipe(
      tap(_ => this.log(`Logged in user: ${user.username}`)),
      catchError(this.handleError<any>('loginUser'))
    );
  }

  // Error Handling
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  // Logs data
  private log(message: string) {
    console.log(`HeroService: ${message}`);
  }

}
