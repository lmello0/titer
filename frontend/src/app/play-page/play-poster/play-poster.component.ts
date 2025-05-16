import { Component, Input } from '@angular/core';
import { CompactNumberPipe } from './pipe/compact-number.pipe';

@Component({
  selector: 'app-play-poster',
  imports: [CompactNumberPipe],
  templateUrl: './play-poster.component.html',
  styleUrl: './play-poster.component.css',
})
export class PlayPosterComponent {
  @Input() url!: string;
  @Input() rating!: number;
  @Input() watchCount!: number;
  @Input() reviewCount!: number;
}
