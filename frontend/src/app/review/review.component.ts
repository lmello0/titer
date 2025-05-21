import { CommonModule } from '@angular/common';
import { Component, input, Input, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Review } from '../shared/interfaces/review.interface';
import { PlayService } from '../services/play/play.service';
import { PlayDetails } from '../shared/interfaces/play-details.interface';

@Component({
  selector: 'app-review',
  imports: [CommonModule, RouterLink],
  templateUrl: './review.component.html',
  styleUrl: './review.component.css',
})
export class ReviewComponent implements OnInit {
  playData!: PlayDetails;
  @Input() reviewData!: Review;

  constructor(private readonly playService: PlayService) {}

  ngOnInit(): void {
    this.playService.getPlayById(this.reviewData.id).subscribe((p) => {
      if (!p) return;

      this.playData = p;
    });
  }

  get fullStars(): number[] {
    return Array(Math.floor(this.reviewData.rating));
  }

  get hasHalfStar(): boolean {
    return this.reviewData.rating % 1 >= 0.5;
  }
}
