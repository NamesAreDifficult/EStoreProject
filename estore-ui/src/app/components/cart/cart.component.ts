import { CartBeef, CartServiceService } from 'src/app/services/cartService/cart-service.service';
import { LoggingService } from 'src/app/services/loggingService/logging.service';
import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Beef } from '../../services/beefService/beef.service';
import { CustomerAuthenticationService } from 'src/app/services/customerAuthService/customer-authentication.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent {

  cart$!: Observable<Beef[]>

  constructor(private shoppingService: CartServiceService, private customerAuthenticationService: CustomerAuthenticationService, private logger: LoggingService) {

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
