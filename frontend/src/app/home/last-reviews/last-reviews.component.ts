import { Component, OnInit } from '@angular/core';
import { ReviewComponent } from '../../review/review.component';
import { Review } from '../../shared/interfaces/review.interface';
import { RouterLink } from '@angular/router';
import { PlayService } from '../../services/play/play.service';

@Component({
  selector: 'app-last-reviews',
  imports: [ReviewComponent, RouterLink],
  templateUrl: './last-reviews.component.html',
  styleUrl: './last-reviews.component.css',
})
export class LastReviewsComponent implements OnInit {
  lastReviews: Review[] = [];

  constructor(private readonly playService: PlayService) {}

  ngOnInit(): void {
    this.playService.getRecentReviews().subscribe((reviews) => {
      this.lastReviews = reviews;
    });
  }
}
