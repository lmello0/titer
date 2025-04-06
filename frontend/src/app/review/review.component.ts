import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { min } from 'rxjs';

@Component({
  selector: 'app-review',
  imports: [CommonModule],
  templateUrl: './review.component.html',
  styleUrl: './review.component.css',
})
export class ReviewComponent {
  @Input() title = 'The Shawshank Redemption';
  @Input() year =
    Math.floor(Math.random() * (new Date().getFullYear() - 1930 + 1)) + 1930;

  rating = 0.5 + Math.floor(Math.random() * 10) * 0.5;
  numLikes = Math.floor(Math.random() * (10_000 - 1_00 + 1)) + 1_00;

  get fullStars(): number[] {
    return Array(Math.floor(this.rating));
  }

  get hasHalfStar(): boolean {
    return this.rating % 1 >= 0.5;
  }
}
