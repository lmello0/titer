import { Component, Input, OnInit } from '@angular/core';
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
}
