import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-tab-crew',
  imports: [RouterLink],
  templateUrl: './tab-crew.component.html',
  styleUrl: './tab-crew.component.css',
})
export class TabCrewComponent {
  @Input() crew!: { id: number; name: string; role: string }[];
}
