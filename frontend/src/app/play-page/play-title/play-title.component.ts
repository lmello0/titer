import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Director } from '../../shared/interfaces/director';

@Component({
  selector: 'app-play-title',
  imports: [RouterLink],
  templateUrl: './play-title.component.html',
  styleUrl: './play-title.component.css',
})
export class PlayTitleComponent {
  @Input() title!: string;
  @Input() director!: Director;
  @Input() year!: number;
  @Input() synopsis!: string;
}
