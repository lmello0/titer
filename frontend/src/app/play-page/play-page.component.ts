import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-play-page',
  imports: [RouterLink],
  templateUrl: './play-page.component.html',
  styleUrl: './play-page.component.css',
})
export class PlayPageComponent implements OnInit {
  formatter = Intl.NumberFormat('en', { notation: 'compact' });

  title: string = 'Spirited Away';
  director: string = 'Hayao Miyazaki';
  year: number = 2001;
  rating: number = 1 + Math.round(Math.random() * 5 * 10) / 10;
  watchCount: string = this.formatter.format(
    Math.floor(Math.random() * (1_000_000 * 10)),
  );
  reviewCount: string = this.formatter.format(
    Math.floor(Math.random() * (1_000_000 * 10)),
  );
  url: string =
    'https://image.tmdb.org/t/p/w600_and_h900_bestv2/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg';

  playId!: string;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.playId = this.route.snapshot.paramMap.get('id')!;
  }
}
