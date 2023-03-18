import { Component, OnInit } from '@angular/core';
import { Beef, BeefService } from '../../services/beefService/beef.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {

  constructor(private beefService: BeefService) {}
}
