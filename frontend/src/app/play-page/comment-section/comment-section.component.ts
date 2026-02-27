import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Review } from '../../shared/interfaces/review.interface';
import { CommonModule } from '@angular/common';
import { PostReviewComponent } from './post-review/post-review.component';
import { CommentListComponent } from './comment-list/comment-list.component';
import { LoginToReviewItComponent } from './login-to-review-it/login-to-review-it.component';
import { REVIEW_SORT_OPTIONS, ReviewSortOption } from '../../shared/interfaces/review-sort-option.type';

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
export class CommentSectionComponent implements OnChanges {
  @Input() playId = 0;
  @Input() comments: Review[] = [];

  isLoggedIn = false;
  sortOptions = REVIEW_SORT_OPTIONS;
  currentSort: ReviewSortOption = 'MOST_RECENT';
  displayedComments: Review[] = [];

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['comments']) {
      this.applySort(this.currentSort);
    }
  }

  addReviewToComments(comment: { content: string; rating: number }): void {
    const nextId = this.comments.length > 0 ? Math.max(...this.comments.map((value) => value.id)) + 1 : 1;

    const newReview: Review = {
      id: nextId,
      author: {
        userId: 999,
        username: 'you',
        profilePhotoUrl: 'https://i.pravatar.cc/150?img=61',
      },
      date: new Date(),
      content: comment.content,
      likes: 0,
      likedByUser: false,
      rating: comment.rating,
      playId: this.playId,
      numComments: 0,
    };

    this.comments = [newReview, ...this.comments];
    this.applySort(this.currentSort);
  }

  applySort(sortBy: ReviewSortOption): void {
    this.currentSort = sortBy;

    const sorted = [...this.comments];

    switch (sortBy) {
      case 'TOP_RATED':
        sorted.sort((a, b) => b.rating - a.rating || b.likes - a.likes);
        break;
      case 'MOST_LIKED':
        sorted.sort((a, b) => b.likes - a.likes || b.date.getTime() - a.date.getTime());
        break;
      case 'MOST_RECENT':
      default:
        sorted.sort((a, b) => b.date.getTime() - a.date.getTime());
        break;
    }

    this.displayedComments = sorted;
  }
}
