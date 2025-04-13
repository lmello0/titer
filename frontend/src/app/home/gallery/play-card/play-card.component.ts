import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-play-card',
  imports: [],
  templateUrl: './play-card.component.html',
  styleUrl: './play-card.component.css',
})
export class PlayCardComponent implements OnInit {
  @Input() playTitle!: string;
  @Input() reviews!: number | string;
  @Input() watched!: number | string;
  @Input() url!: string;

  ngOnInit(): void {
    const formatter = Intl.NumberFormat('en', { notation: 'compact' });

    this.reviews = formatter.format(this.reviews as number);

    this.watched = formatter.format(this.watched as number);
  }
}
