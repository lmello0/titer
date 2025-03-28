import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-play-card',
  imports: [],
  templateUrl: './play-card.component.html',
  styleUrl: './play-card.component.css',
})
export class PlayCardComponent implements OnInit {
  @Input() reviews!: string;
  @Input() watched!: string;
  @Input() url: string =
    'https://m.media-amazon.com/images/I/81kz06oSUeL._AC_SL1500_.jpg';

  ngOnInit(): void {
    const formatter = Intl.NumberFormat('en', { notation: 'compact' });

    this.reviews = formatter.format(
      Math.floor(Math.random() * (1_000_000 * 10)),
    );

    this.watched = formatter.format(
      Math.floor(Math.random() * (1_000_000 * 10)),
    );
  }
}
