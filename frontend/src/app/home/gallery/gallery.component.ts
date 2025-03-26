import { Component, Input } from '@angular/core';
import { PlayCardComponent } from './play-card/play-card.component';

@Component({
  selector: 'app-gallery',
  imports: [PlayCardComponent],
  templateUrl: './gallery.component.html',
  styleUrl: './gallery.component.css',
})
export class GalleryComponent {
  @Input() title: string = '';

  items = [1, 2, 3, 4, 5, 6];
}
