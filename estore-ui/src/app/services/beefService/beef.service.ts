import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { LoggingService } from '../loggingService/logging.service';

export interface Beef{
  id: number;
  cut: string;
  grade: string;
  weight: number;
  price: number;
}

@Injectable({
  providedIn: 'root'
})
export class BeefService {
  private apiUrl = "http://localhost:8080/inventory/products"

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  }

  constructor(
    private http: HttpClient,
    private loggingService: LoggingService
  ) { }

  // Gets all Beef products that currently exist within the inventory
  getAllBeef(): Observable<Beef[]> {
    return this.http.get<Beef[]>(this.apiUrl)
      .pipe(
        tap(_=> this.log('fetched Beef')),
        catchError(this.handleError<Beef[]>('getAllBeef', []))
      );
  }

  // Get beef with given ID
  getBeef(id: number): Observable<Beef> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<Beef>(url).pipe(
      tap(_ => this.log(`fetched Beef id=$[id}`)),
      catchError(this.handleError<Beef>(`getBeef id=${id}`))
    );
  }

  // Search beef name with the provided term
  searchBeef(term: string): Observable<Beef[]> {
    // if the search term is empty
    if (!term.trim()) {
      return this.getAllBeef();
    }
    return this.http.get<Beef[]>(`${this.apiUrl}/?name=${term}`)
      .pipe(
        tap(x => x.length ?
          this.log(`found beef matching ${term}`) :
          this.log(`no beef matching ${term}`)),
        catchError(this.handleError<Beef[]>(`searchBeef`, []))
      );
  }

  // add new Beef item tot he inventory
  addBeef(beef: Beef): Observable<Beef> {
    return this.http.post<Beef>(this.apiUrl, beef, this.httpOptions).pipe(
      tap((newBeef: Beef) => this.log(`added beef with id=${newBeef.id}`)),
      catchError(this.handleError<Beef>(`addBeef`))
    );
  }

  // Delete a beef item from the inventory
  deleteBeef(id: number):Observable<Beef> {
    const url = `${this.apiUrl}/${id}`;

    return this.http.delete<Beef>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted hero id=${id}`)),
      catchError(this.handleError<Beef>(`deleteBeef`))
    );
  }

  // Update a beef item matching the provided beef object
  updateBeef(beef: Beef): Observable<any> {
    return this.http.put(this.apiUrl, beef, this.httpOptions).pipe(
      tap(_ => this.log(`Updated beef id=${beef.id}`)),
      catchError(this.handleError<any>(`updateBeef`))
    );
  }


  //Error handling for failed operations
  private handleError<T>(operation = `operation`, result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);

      this.log(`${operation} failed: ${error.message}`);

      return of(result as T);
    }
  }

  //Logging service handler
  private log(message: string) {
    this.loggingService.add(`BeefService: ${message}`);
  }
}
