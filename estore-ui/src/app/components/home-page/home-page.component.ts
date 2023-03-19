import { Component } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Beef, BeefService } from '../../services/beefService/beef.service';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {

  beef: Beef[] = [];
  
  constructor(private beefService: BeefService) {}
  
  getAllBeef(): void {
    this.beefService.getAllBeef()
        .subscribe(beef => this.beef = beef.slice(0,4));

  } 

  ngOnInit(): void {
    this.getAllBeef();
  }
}
