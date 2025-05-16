import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-tab-releases',
  imports: [],
  templateUrl: './tab-releases.component.html',
  styleUrl: './tab-releases.component.css',
})
export class TabReleasesComponent {
  @Input() releases!: { date: string; names: string[] }[];
}
