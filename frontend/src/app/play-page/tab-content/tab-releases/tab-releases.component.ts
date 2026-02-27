import { Component, Input } from '@angular/core';
import { Release } from '../../../shared/interfaces/release.interface';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-tab-releases',
  imports: [CommonModule, RouterLink],
  templateUrl: './tab-releases.component.html',
  styleUrl: './tab-releases.component.css',
})
export class TabReleasesComponent {
  @Input() releases!: Release[];
}
