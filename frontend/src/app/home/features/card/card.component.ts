import { Component, Input } from '@angular/core';
import { Card } from './card.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-card',
  imports: [CommonModule],
  templateUrl: './card.component.html',
  styleUrl: './card.component.css',
})
export class CardComponent implements Card {
  @Input() icon!: string;
  @Input() description!: string;
  @Input() hoverColor!: string;
}
