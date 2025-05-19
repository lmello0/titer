import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Worker } from '../../../shared/interfaces/worker';

@Component({
  selector: 'app-tab-crew',
  imports: [RouterLink],
  templateUrl: './tab-crew.component.html',
  styleUrl: './tab-crew.component.css',
})
export class TabCrewComponent {
  @Input() crew!: Worker[];
}
