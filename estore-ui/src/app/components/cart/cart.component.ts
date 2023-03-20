import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Beef, BeefService } from 'src/app/services/beefService/beef.service';
import { CartBeef, CartServiceService } from 'src/app/services/cartService/cart-service.service';
import { LoggingService } from 'src/app/services/loggingService/logging.service';
import { User, UserService } from 'src/app/services/userService/user.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent {

  cart$!: Observable<Beef[]>

  constructor(private shoppingService: CartServiceService, private logger: LoggingService) {

  }
  ngOnInit() {
    this.cart$ = this.shoppingService.getCart();
  }

}
