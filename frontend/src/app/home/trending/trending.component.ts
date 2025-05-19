import { Component, OnInit } from '@angular/core';
import { GalleryComponent } from '../gallery/gallery.component';
import { PlayDetails } from '../../shared/interfaces/play-details.interface';
import { mockPlays } from '../../shared/mock/mock-plays';

@Component({
  selector: 'app-trending',
  imports: [GalleryComponent],
  templateUrl: './trending.component.html',
  styleUrl: './trending.component.css',
})
export class TrendingComponent implements OnInit {
  mockPlays: PlayDetails[] = mockPlays();

  plays: PlayDetails[] = [];

  ngOnInit() {
    for (let i = 0; i < 5; i++) {
      const randomPlay =
        this.mockPlays[Math.floor(Math.random() * this.mockPlays.length)];

      this.plays.push(randomPlay);

      const rmIndex = this.mockPlays.findIndex(
        (mp) => mp.playId === randomPlay.playId,
      );
      this.mockPlays.splice(rmIndex, 1);
    }
  }
}
