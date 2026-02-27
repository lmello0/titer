import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PlayDetails } from '../../../shared/interfaces/play-details.interface';
import { CompactNumberPipe } from '../../../shared/pipe/compact-number/compact-number.pipe';

@Component({
  selector: 'app-play-card',
  imports: [RouterLink, CompactNumberPipe],
  templateUrl: './play-card.component.html',
  styleUrl: './play-card.component.css',
})
export class PlayCardComponent {
  @Input() play!: PlayDetails;

  get statusClass(): string {
    if (this.play.status === 'verified') {
      return 'fa-solid fa-check ui-badge-verified';
    }

    if (this.play.status === 'unverified') {
      return 'fa-solid fa-question ui-badge-unverified';
    }

    return 'fa-solid fa-x ui-badge-rejected';
  }
}
