import { Component, OnInit } from '@angular/core';
import { GalleryComponent } from '../gallery/gallery.component';
import { PlayDetails } from '../../shared/interfaces/play-details.interface';
import { PlayService } from '../../services/play/play.service';

@Component({
  selector: 'app-trending',
  imports: [GalleryComponent],
  templateUrl: './trending.component.html',
  styleUrl: './trending.component.css',
})
export class TrendingComponent implements OnInit {
  plays: PlayDetails[] = [];

  constructor(private readonly playService: PlayService) {}

  ngOnInit() {
    this.playService.getTrendingPlays().subscribe((p) => (this.plays = p));
  }
}
