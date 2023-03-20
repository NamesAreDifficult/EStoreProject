import { Component } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';

import { Beef, BeefService } from '../../services/beefService/beef.service';

@Component({
  selector: 'app-search',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent {
  beef$!: Observable<Beef[]>;
  private searchTerms = new BehaviorSubject<string>('');
  selectedBeef?: Beef;

  constructor(private beefService: BeefService) {
  }

  OnSelect(beef: Beef): void {
    this.selectedBeef=beef;
  }
  search(term: string): void{
    this.searchTerms.next(term);
  }

  ngOnInit(): void{
    // The pipe operations cause a 300 ms delay between keystrokes, waits until the input has changed for the next search and remaps the observable once the search is completed
    this.searchWatcher();
  }


  searchWatcher = async(): Promise<void> => {
    this.beef$ = this.searchTerms.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap((term: string) => this.beefService.searchBeef(term)),
    );
  }
}