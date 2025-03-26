import { Component } from '@angular/core';

@Component({
  selector: 'app-play-card',
  imports: [],
  templateUrl: './play-card.component.html',
  styleUrl: './play-card.component.css',
})
export class PlayCardComponent {
  reviews: number = Math.floor(Math.random() * Number.MAX_SAFE_INTEGER);
  watched: number = Math.floor(Math.random() * Number.MAX_SAFE_INTEGER);
}
