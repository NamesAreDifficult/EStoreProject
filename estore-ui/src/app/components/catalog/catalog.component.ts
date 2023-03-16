import { Component, OnInit } from '@angular/core';
import { BeefService, Beef } from '../../services/beefService/beef.service'

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent {
  beefArray: Beef[] = [];

  constructor(private beefService: BeefService){}

  ngOnInit(): void{
    this.getBeef();
  }

  getBeef(): void{
    this.beefService.getAllBeef()
      .subscribe(beef => this.beefArray = beef);
  }

  searchBeef(term: string): void{
    if(term == ""){
      this.getBeef()
    }
    this.beefService.searchBeef(`${term}`)
      .subscribe(beef => this.beefArray = beef);
  }
}
