import { Component } from '@angular/core';
import { ReviewComponent } from '../../review/review.component';

@Component({
  selector: 'app-last-reviews',
  imports: [ReviewComponent],
  templateUrl: './last-reviews.component.html',
  styleUrl: './last-reviews.component.css',
})
export class LastReviewsComponent {
  lastReviews: any[] = [1, 2, 3, 4, 5, 6];
}
