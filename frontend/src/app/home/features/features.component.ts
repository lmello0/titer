import { Component } from '@angular/core';
import { CardComponent } from './card/card.component';
import { Card } from './card/card.interface';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-features',
  imports: [CommonModule, CardComponent, RouterLink],
  templateUrl: './features.component.html',
  styleUrl: './features.component.css',
})
export class FeaturesComponent {
  cards: Card[] = [
    {
      icon: 'fa-eye',
      description: 'Track plays you have watched or plan to watch.',
      hoverColor: 'hover:bg-blue-500',
    },
    {
      icon: 'fa-pencil',
      description: 'Write reviews to help others discover great plays',
      hoverColor: 'hover:bg-green-500',
    },
    {
      icon: 'fa-share',
      description: 'Share your favorite plays with friends.',
      hoverColor: 'hover:bg-red-500',
    },

    {
      icon: 'fa-list',
      description: 'Create and share lists of you recommended plays.',
      hoverColor: 'hover:bg-blue-500',
    },
    {
      icon: 'fa-calendar',
      description: 'View upcoming play schedules. Coming soon!',
      hoverColor: 'hover:bg-green-500',
    },
    {
      icon: 'fa-building',
      description: 'Discover theaters near you. Coming soon!',
      hoverColor: 'hover:bg-red-500',
    },
  ];
}
