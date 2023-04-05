import { Component, Input } from '@angular/core';
import { Beef, BeefService } from '../../services/beefService/beef.service';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { CartServiceService } from 'src/app/services/cartService/cart-service.service';



@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent {

  @Input() beef?: Beef;

  productAlert: String = ""

  Observer = {
    next: (beef: Beef) => {
      this.beef = beef;
      const id = Number(this.route.snapshot.paramMap.get('id'));
    },
    error: (err: Error) => (this.catchStatusCode(Number(err.message)))
  }

  AddCartObserver = {
    next: (beef: Beef) => {
      this.beef = beef;
      const id = Number(this.route.snapshot.paramMap.get('id'));
    },
    error: (err: Error) => (this.catchStatusCode(Number(err.message))),
    complete: () => this.getBeef()
  }

  private catchStatusCode(code: number) {
    if (code == 404) {
      this.productAlert = "Product does not exist, please click on an existing product"
    }
    else if (code == 400) {
      this.productAlert = "Please enter positive values only"
    }
    else if (code == 409) {
      this.productAlert = "Not enough available to add to cart"
    }
  }
  constructor(
    private route: ActivatedRoute,
    private beefService: BeefService,
    private location: Location,
    private shoppingService: CartServiceService
  ) { }

  ngOnInit(): void {
    this.getBeef();
  }

  getBeef(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.beefService.getBeef(id)
      .subscribe(this.Observer);
  }

  goBack(): void {
    this.location.back();
  }

  public addToCart(id: number, amount: string) {
    var amount_number = Number(amount)

    if (isNaN(amount_number)) {
      this.productAlert = "Please enter numbers only"
      return null;
    }



    this.shoppingService.addToCart(
      {
        id: id,
        weight: Number(amount)
      }
    ).subscribe(this.AddCartObserver)
    this.productAlert = "Item added to your shopping cart"
    return null;
  }
}

