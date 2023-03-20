import { Component, Input } from '@angular/core';
import { Beef } from '../../services/beefService/beef.service';



@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent {

  @Input() beef?: Beef;
}
