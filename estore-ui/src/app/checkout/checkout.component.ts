import { CartServiceService } from 'src/app/services/cartService/cart-service.service';
import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Beef } from 'src/app/services/beefService/beef.service';
import { LoggingService } from 'src/app/services/loggingService/logging.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent {
  checkoutAlert = ""

  cart$!: Observable<Beef[]>

  constructor(private shoppingService: CartServiceService, private logger: LoggingService) {

  }

  ngOnInit() {
    this.cart$ = this.shoppingService.getCart();
  }

  checkout(){
    this.shoppingService.checkout().subscribe(
      {
        next: (any: any) => this.cart$ = this.shoppingService.checkout()
      }
    )
    this.checkoutAlert = "Thanks for shopping at Cow-Related Pun!"
  }
}
