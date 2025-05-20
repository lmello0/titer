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
  @Input() rating = 0;

  hoverValue: number | null = null;

  toggleLike(comment: Comment): void {
    comment.likedByUser = !comment.likedByUser;
    comment.likes += comment.likedByUser ? 1 : -1;
  }

  autoResize(event: Event): void {
    const textarea = event.target as HTMLTextAreaElement;

    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
  }

  setRating(newRating: number) {
    this.rating = newRating;
  }

  setHover(value: number | null) {
    this.hoverValue = value;
  }

  getFillPercentage(starIndex: number): number {
    const value = this.hoverValue ?? this.rating;
    const diff = value - starIndex;

    if (diff >= 1) return 100;
    if (diff >= 0.5) return 50;
    if (diff > 0) return diff * 100;

    return 0;
  }
}
