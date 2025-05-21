import { Component, Input } from '@angular/core';
import { Review } from '../../shared/interfaces/review.interface';
import { CommonModule } from '@angular/common';
import { PostReviewComponent } from './post-review/post-review.component';
import { CommentListComponent } from './comment-list/comment-list.component';

@Component({
  selector: 'app-comment-section',
  imports: [CommonModule, PostReviewComponent, CommentListComponent],
  templateUrl: './comment-section.component.html',
  styleUrl: './comment-section.component.css',
})
export class CommentSectionComponent {
  @Input() comments!: Review[];
  rating = 0;

  addReviewToComments(comment: { content: string; rating: number }): void {
    this.comments.push(comment as unknown as Review);
  }
}
