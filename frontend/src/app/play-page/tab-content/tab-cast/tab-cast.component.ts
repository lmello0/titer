import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-tab-cast',
  imports: [RouterLink],
  templateUrl: './tab-cast.component.html',
  styleUrl: './tab-cast.component.css',
})
export class TabCastComponent {
  @Input() cast!: { id: number; name: string; character: string }[];
}
