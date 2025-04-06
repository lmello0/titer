import { CommonModule } from '@angular/common';
import { Component, input, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Review } from './review.interface';

@Component({
  selector: 'app-review',
  imports: [CommonModule, RouterLink],
  templateUrl: './review.component.html',
  styleUrl: './review.component.css',
})
export class ReviewComponent {
  @Input() reviewData!: Review;

  get fullStars(): number[] {
    return Array(Math.floor(this.reviewData.rating));
  }

  get hasHalfStar(): boolean {
    return this.reviewData.rating % 1 >= 0.5;
  }
}
