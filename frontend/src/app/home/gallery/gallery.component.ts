import { Component, Input, OnInit } from '@angular/core';
import { PlayCardComponent } from './play-card/play-card.component';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { PlayDetails } from '../../shared/interfaces/play-details.interface';

@Component({
  selector: 'app-gallery',
  imports: [CommonModule, PlayCardComponent, RouterLink],
  templateUrl: './gallery.component.html',
  styleUrl: './gallery.component.css',
})
export class GalleryComponent {
  @Input() galleryTitle!: string;
  @Input() route!: string;
  @Input() plays!: PlayDetails[];
}
