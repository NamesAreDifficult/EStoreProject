import {CartServiceService } from 'src/app/services/cartService/cart-service.service';
import { LoggingService } from 'src/app/services/loggingService/logging.service';
import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Beef } from '../../services/beefService/beef.service';
import { isEmpty } from 'rxjs/operators';  

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent {

  cart$!: Observable<Beef[]>

  cartAlert = ""

  constructor(private shoppingService: CartServiceService, private logger: LoggingService) {

  }
  
  ngOnInit() {
    this.cart$ = this.shoppingService.getCart();
  }

  removeFromCart(id: number) {
    this.shoppingService.removeFromCart(id).subscribe(
      {
        next: (any: any) => this.cart$ = this.shoppingService.getCart()
      }
    )
  }

}
