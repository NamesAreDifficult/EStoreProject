import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { throwError, Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { LoggingService } from '../loggingService/logging.service';


export interface Beef {
  id?: number;
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
        tap(_ => this.log('fetched Beef')),
        catchError(this.loggingService.handleError<Beef[]>('getAllBeef', []))
      );
  }

  // Get beef with given ID
  getBeef(id: number): Observable<Beef> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<Beef>(url).pipe(
      tap(_ => this.log(`fetched Beef id=$[id}`)),
      catchError(this.loggingService.handleError<Beef>(`getBeef id=${id}`))
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
        catchError(this.loggingService.handleError<Beef[]>(`searchBeef`, []))
      );
  }

  // add new Beef item tot he inventory
  addBeef(beef: Beef): Observable<Beef> {
    return this.http.post<Beef>(this.apiUrl, beef, this.httpOptions).pipe(
      tap((newBeef: Beef) => this.log(`added beef with id=${newBeef.id}`)),
      catchError(err => {
        this.loggingService.handleError<any>('addBeef')
        return throwError((() => new Error(err.status)));
      })
    );
  }

  // Delete a beef item from the inventory
  deleteBeef(id: number): Observable<Beef> {
    const url = `${this.apiUrl}/${id}`;

    return this.http.delete<Beef>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted beef id=${id}`)),
      catchError(err => {
        this.loggingService.handleError<any>('deleteBeef')
        return throwError((() => new Error(err.status)));
      })
    );
  }

  // Update a beef item matching the provided beef object
  updateBeef(beef: Beef): Observable<any> {
    return this.http.put(this.apiUrl, beef, this.httpOptions).pipe(
      tap(_ => this.log(`Updated beef id=${beef.id}`)),

      catchError(err => {
        this.loggingService.handleError<any>('updateBeef')
        return throwError((() => new Error(err.status)));
      })
    );
  }

  //Logging service handler
  private log(message: string) {
    this.loggingService.add(`BeefService: ${message}`);
  }
}
