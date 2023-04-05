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
    // Checks if user is at max amount of cards
    this.cards$ = this.cardService.getCards().pipe(
      tap((cardItems: CreditCard[]) =>{
        this.isMax = cardItems.length === 3;
      })
    );
  }

  // Catches error code for add method and displays text
  private addStatus(code: number) {
    // Conflict error
    if (code == 400) {
      this.cardAlert = "Invalid card attributes."
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
        this.cardAlert = "Card already exists on this account."
      }
    // Internal server error
    } else if (code == 500) {
      this.cardAlert = "Internal server error."
    }
  }

  // Catches error code for remove card method and displays text
  private removeStatus(code: number) {
    // Conflict error
    if (code == 409) {
      this.cardAlert = "Card does not exist on user."
    // Creating item with invalid fields
    } else if (code == 404) {
      this.cardAlert = "User/card not found."
      // Handles existant card or max cards
    } else if (code == 500) {
      this.cardAlert = "Internal server error"
      // Handles empty credit card lists
    } else if (code == 400) {
      this.cardAlert = "You have no credit cards."
    }
  }

  // Observer for handling addition of new cards
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

  // Observer for handling removal of existing cards
  removeObserver = {
    next: (card: CreditCard) => {
      this.cardService.removeCard(card.number)
    },
    error: (err: Error) => (this.removeStatus(Number(err.message))),
    complete: () => this.cards$ = this.cardService.getCards().pipe(
      tap((cardItems: CreditCard[]) =>{
        this.isMax = cardItems.length === 3;
      })
    )
  }

  // Adds card if attributes are valid and user has space
  addCard(number: string, expiration: string, cvv: string) {
    var card: CreditCard = {
      number: number.trim(),
      expiration: expiration,
      cvv: cvv
    }
      
    this.cardService.addCard(card).subscribe(this.addObserver)
    this.cardAlert = "Card created."
    
  }

  // Removes card if it exists on user
  removeCard(number: string) {
    this.cardService.removeCard(number).subscribe(this.removeObserver);
    this.cardAlert = "Card deleted."
  }
}
