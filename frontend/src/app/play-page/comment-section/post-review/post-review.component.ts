import { CommonModule } from '@angular/common';
import { Component, EventEmitter, inject, Output } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

@Component({
  selector: 'app-post-review',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './post-review.component.html',
  styleUrl: './post-review.component.css',
})
export class PostReviewComponent {
  @Output() submitReview = new EventEmitter<{
    content: string;
    rating: number;
  }>();

  private formBuilder = inject(FormBuilder);

  reviewForm = this.formBuilder.group({
    content: ['', [Validators.required, Validators.minLength(5)]],
    rating: [0, [Validators.required, Validators.min(0.5)]],
  });

  content = new FormControl('');
  rating = 0;
  hover: number | null = null;

  autoResize(event: Event): void {
    const textarea = event.target as HTMLTextAreaElement;
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
  }

  setHover(value: number | null): void {
    this.hover = value;
  }

  setRating(value: number): void {
    this.rating = value;
    this.reviewForm.get('rating')?.setValue(this.rating);
  }

  getFillPercentage(star: number): number {
    if (this.hover !== null) {
      return Math.min(Math.max((this.hover - star) * 100, 0), 100);
    }
    return Math.min(Math.max((this.rating - star) * 100, 0), 100);
  }

  postReview(): void {
    if (this.reviewForm.invalid) return;

    this.submitReview.emit(
      this.reviewForm.value as { content: string; rating: number },
    );
    this.content.setValue('');
    this.rating = 0;
    this.hover = null;
  }
}
