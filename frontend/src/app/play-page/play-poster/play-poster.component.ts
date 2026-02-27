import { Component, Input } from '@angular/core';
import { CompactNumberPipe } from '../../shared/pipe/compact-number/compact-number.pipe';
import { PlayStatus } from '../../shared/interfaces/play-details.interface';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-play-poster',
  imports: [CompactNumberPipe, DecimalPipe],
  templateUrl: './play-poster.component.html',
  styleUrl: './play-poster.component.css',
})
export class PlayPosterComponent {
  @Input() url!: string;
  @Input() rating!: number;
  @Input() watchCount!: number;
  @Input() reviewCount!: number;
  @Input() status!: PlayStatus;

  get statusClass(): string {
    if (this.status === 'verified') {
      return 'ui-badge ui-badge-verified';
    }

    if (this.status === 'unverified') {
      return 'ui-badge ui-badge-unverified';
    }

    return 'ui-badge ui-badge-rejected';
  }
}
