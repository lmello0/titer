import { Component } from '@angular/core';
import { GalleryComponent } from '../gallery/gallery.component';

@Component({
  selector: 'app-most-popular-all-time',
  imports: [GalleryComponent],
  templateUrl: './most-popular-all-time.component.html',
  styleUrl: './most-popular-all-time.component.css',
})
export class MostPopularAllTimeComponent {}
