import { Component } from '@angular/core';
import { CardComponent } from './card/card.component';
import { Card } from './card/card.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-features',
  imports: [CommonModule, CardComponent],
  templateUrl: './features.component.html',
  styleUrl: './features.component.css',
})
export class FeaturesComponent {
  cards: Card[] = [
    {
      icon: 'fa-eye',
      description: 'Mark the plays that you watched or will watch',
      hoverColor: 'hover:bg-blue-500',
    },
    {
      icon: 'fa-pencil',
      description: 'Write a review for the others to know if the play is good',
      hoverColor: 'hover:bg-green-500',
    },
    {
      icon: 'fa-share',
      description: 'Share the plays that you like with your friends',
      hoverColor: 'hover:bg-red-500',
    },

    {
      icon: 'fa-list',
      description:
        'Create lists with plays that you like and recommend and share it with everyone',
      hoverColor: 'hover:bg-blue-500',
    },
    {
      icon: 'fa-calendar',
      description: 'Find out the plays calendar ???',
      hoverColor: 'hover:bg-green-500',
    },
    {
      icon: 'fa-building',
      description: 'See theaters near you ???',
      hoverColor: 'hover:bg-red-500',
    },
  ];
}
