import { Component, Input } from '@angular/core';
import { Comment } from '../../shared/interfaces/comment.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-comment-section',
  imports: [CommonModule],
  templateUrl: './comment-section.component.html',
  styleUrl: './comment-section.component.css',
})
export class CommentSectionComponent {
  @Input() comments!: Comment[];

  toggleLike(comment: Comment): void {
    comment.likedByUser = !comment.likedByUser;
    comment.likes += comment.likedByUser ? 1 : -1;
  }
}
