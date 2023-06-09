import { CartServiceService } from 'src/app/services/cartService/cart-service.service';
import { Component } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Beef } from 'src/app/services/beefService/beef.service';
import { LoggingService } from 'src/app/services/loggingService/logging.service';
import { CardService, CreditCard } from 'src/app/services/cardService/card.service';
import { User, UserService } from 'src/app/services/userService/user.service';
import { isEmpty } from 'rxjs/operators';
@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent {
  checkoutAlert = ""
  cart$!: Observable<Beef[]>
  isEmpty!: boolean;
  cards$!: Observable<CreditCard[]>
  currentCard: CreditCard | undefined;
  user!: User | null;
  total: number = 0
  constructor(private shoppingService: CartServiceService, private userService: UserService,
    private cardService: CardService) {
  }

  ngOnInit() {
    this.cards$ = this.cardService.getCards()
    this.cart$ = this.shoppingService.getCart().pipe(
      tap((cartItems: Beef[]) => {
        this.isEmpty = cartItems.length === 0;
      })
    );
    this.cart$.subscribe(cart => {
      this.total = 0
      for (var item of cart) {
        this.total += (item.price * item.weight)
      }
    })
    this.userService.userNotifier.subscribe(currentUser => {
      this.user = currentUser;
      if (currentUser == null) {
        location.reload()
      }
    });
  }

  selectCard(card: CreditCard) {
    this.currentCard = card
  }

  checkout() {
    if (this.currentCard === undefined) {
      this.checkoutAlert = "Please select a credit card"
    }
    else {
      this.shoppingService.checkout(this.currentCard.number).subscribe(
        {
          next: (any: any) => this.cart$ = this.shoppingService.checkout(this.currentCard!.number)
        }
      )
      document.getElementById("checkoutButton")!.style.display = "none";
      document.getElementById("cardsHead")!.style.display = "none";
      document.getElementById("cardsMenu")!.style.display = "none";
      document.getElementById("orderTitle")!.style.display = "none";
      document.getElementById("order")!.style.display = "none";
      this.checkoutAlert = "Thanks for shopping at Cow-Related Pun!"
    }
  }
}
