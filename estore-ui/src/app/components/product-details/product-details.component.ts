import { Component, Input } from '@angular/core';
import { Beef, BeefService } from '../../services/beefService/beef.service';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';



@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent {

  @Input() beef?: Beef;

  constructor(
    private route: ActivatedRoute,
    private beefService: BeefService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.getBeef();
  }

  getBeef(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.beefService.getBeef(id)
      .subscribe(beef => this.beef = beef);
  }
}
