import { Component, Input } from '@angular/core';
import { Review } from '../../../shared/interfaces/review.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-comment-list',
  imports: [CommonModule],
  templateUrl: './comment-list.component.html',
  styleUrl: './comment-list.component.css',
})
export class CommentListComponent {
  @Input() comments!: Review[];
  @Input() rating = 0;

  toggleLike(comment: Review): void {
    comment.likedByUser = !comment.likedByUser;
    comment.likes += comment.likedByUser ? 1 : -1;
  }
}
