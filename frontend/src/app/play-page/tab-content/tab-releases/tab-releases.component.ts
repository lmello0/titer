import { Component, Input } from '@angular/core';
import { Release } from '../../../shared/interfaces/release';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-tab-releases',
  imports: [CommonModule],
  templateUrl: './tab-releases.component.html',
  styleUrl: './tab-releases.component.css',
})
export class TabReleasesComponent {
  @Input() releases!: Release[];
}
