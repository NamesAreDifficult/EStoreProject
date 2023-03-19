import { Component } from '@angular/core';
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

  constructor(private shoppingService: CartServiceService, private userService: UserService, private logger: LoggingService, private beefService: BeefService) {
    this.cart = [];
    this.getCart();
  }

  public cart: Beef[];

  private getCart() {
    var user: User | null = this.userService.getLoggedIn()
    if (user != null) {
      if (!user.admin) {


        // Code spaghetti which gets all the cart beef values and 
        // Returns their corresponding beef from the backend
        this.shoppingService.getCart(user.username)
          .subscribe(
            {
              next: (cartBeefs: CartBeef[]) => {
                var beefs: Beef[] = new Array(cartBeefs.length);
                cartBeefs.forEach(cartBeef => {
                  this.beefService.getBeef(cartBeef.id).subscribe({
                    next: (beef: Beef) => (beefs.push(beef))
                  }
                  );

                });
                this.cart = beefs;
              },
              error: (err: Error) => (this.logger.handleError(err.message))
            }
          );


      };
    }
  }
}
