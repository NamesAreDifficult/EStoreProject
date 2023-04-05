import { Component } from '@angular/core';
import { CreditCard, CardService } from 'src/app/services/cardService/card.service';
import { Observable, tap } from 'rxjs';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent {
  cards$!: Observable<CreditCard[]>
  cardAlert: String = ""
  isMax!: Boolean

  constructor(private cardService: CardService){}

  ngOnInit(){
    this.cards$ = this.cardService.getCards().pipe(
      tap((cardItems: CreditCard[]) =>{
        this.isMax = cardItems.length === 3;
      })
    );
  }

  // Catches error code for create method and displays text
  private addStatus(code: number) {
    // Conflict error
    if (code == 400) {
      this.cardAlert = "Invalid card attributes"
    // Creating item with invalid fields
    } else if (code == 404) {
      this.cardAlert = "User not found."
      // Handles existant card or max cards
    } else if (code == 409) {
      // Person has max cards
      if (this.isMax){
        this.cardAlert = "You can have up to 3 cards. Please delete one to add a new card."
      }
      // Card already exists
      else{
        this.cardAlert = "Card already exists on this account"
      }
    // Internal server error
    } else if (code == 500) {
      this.cardAlert = "Internal server error"
    }
  }

  addObserver = {
    next: (card: CreditCard) => {
      this.cardService.addCard(card)
    },
    error: (err: Error) => (this.addStatus(Number(err.message))),
    complete: () => this.cards$ = this.cardService.getCards().pipe(
      tap((cardItems: CreditCard[]) =>{
        this.isMax = cardItems.length === 3;
      })
    )
  }

  addCard(number: string, expiration: string, cvv: string) {
    if (true){
      var card: CreditCard = {
        number: number.trim(),
        expiration: expiration,
        cvv: cvv
      }
      
    this.cardService.addCard(card).subscribe(this.addObserver)
    this.cardAlert = "Card created."
    }
  }
}
