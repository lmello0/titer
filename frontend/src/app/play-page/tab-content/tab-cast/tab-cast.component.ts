import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Actor } from '../../../shared/interfaces/actor';

@Component({
  selector: 'app-tab-cast',
  imports: [RouterLink],
  templateUrl: './tab-cast.component.html',
  styleUrl: './tab-cast.component.css',
})
export class TabCastComponent {
  @Input() cast!: Actor[];
}
