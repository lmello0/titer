import { Component, Input } from '@angular/core';
import { Review } from '../../shared/interfaces/review.interface';
import { CommonModule } from '@angular/common';
import { PostReviewComponent } from './post-review/post-review.component';
import { CommentListComponent } from './comment-list/comment-list.component';
import { LoginToReviewItComponent } from './login-to-review-it/login-to-review-it.component';

@Component({
  selector: 'app-comment-section',
  imports: [
    CommonModule,
    PostReviewComponent,
    CommentListComponent,
    LoginToReviewItComponent,
  ],
  templateUrl: './comment-section.component.html',
  styleUrl: './comment-section.component.css',
})
export class CommentSectionComponent {
  @Input() comments!: Review[];
  rating = 0;

  isLoggedIn: boolean = false;

  addReviewToComments(comment: { content: string; rating: number }): void {
    this.comments.push(comment as unknown as Review);
  }
}
