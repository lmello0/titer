import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-play-title',
  imports: [RouterLink],
  templateUrl: './play-title.component.html',
  styleUrl: './play-title.component.css',
})
export class PlayTitleComponent {
  @Input() title!: string;
  @Input() director!: string;
  @Input() year!: number;
  @Input() synopsis!: string;
}
