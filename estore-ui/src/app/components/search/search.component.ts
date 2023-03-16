import { Component, OnInit } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';

import { Beef, BeefService } from '../../services/beefService/beef.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {
  beef$!: Observable<Beef[]>;
  private searchTerms = new Subject<string>();

  constructor(private beefService: BeefService) {}

  search(term: string): void{
    this.searchTerms.next(term);
  }

  ngOnInit(): void{
    // The pipe operations cause a 300 ms delay between keystrokes, waits until the input has changed for the next search and remaps the observable once the search is completed
    this.beef$ = this.searchTerms.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap((term: string) => this.beefService.searchBeef(term))
    );
  }
}
