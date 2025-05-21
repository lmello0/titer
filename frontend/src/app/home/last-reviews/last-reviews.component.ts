import { Component } from '@angular/core';
import { ReviewComponent } from '../../review/review.component';
import { Review } from '../../shared/interfaces/review.interface';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-last-reviews',
  imports: [ReviewComponent, RouterLink],
  templateUrl: './last-reviews.component.html',
  styleUrl: './last-reviews.component.css',
})
export class LastReviewsComponent {
  lastReviews: Review[] = [
    {
      id: 1,
      playId: Math.floor(Math.random() * 10),
      author: {
        userId: 1,
        username: 'John Doe',
        profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
      },
      content:
        'Lorem ipsum dolor sit amet consectetur adipisicing elit. Natus sunt, odio, incidunt nulla quidem repellendus labore, maiores velit delectus earum numquam! Assumenda fuga voluptates expedita accusamus amet placeat veritatis voluptate.',
      date: new Date(),
      likedByUser: false,
      likes: Math.floor(Math.random() * 1_000),
      rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
    },
    {
      id: 1,
      playId: Math.floor(Math.random() * 10),
      author: {
        userId: 1,
        username: 'John Doe',
        profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
      },
      content:
        'Lorem ipsum dolor sit amet consectetur adipisicing elit. Natus sunt, odio, incidunt nulla quidem repellendus labore, maiores velit delectus earum numquam! Assumenda fuga voluptates expedita accusamus amet placeat veritatis voluptate.',
      date: new Date(),
      likedByUser: false,
      likes: Math.floor(Math.random() * 1_000),
      rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
    },
    {
      id: 1,
      playId: Math.floor(Math.random() * 10),
      author: {
        userId: 1,
        username: 'John Doe',
        profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
      },
      content:
        'Lorem ipsum dolor sit amet consectetur adipisicing elit. Natus sunt, odio, incidunt nulla quidem repellendus labore, maiores velit delectus earum numquam! Assumenda fuga voluptates expedita accusamus amet placeat veritatis voluptate.',
      date: new Date(),
      likedByUser: false,
      likes: Math.floor(Math.random() * 1_000),
      rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
    },
    {
      id: 1,
      playId: Math.floor(Math.random() * 10),
      author: {
        userId: 1,
        username: 'John Doe',
        profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
      },
      content:
        'Lorem ipsum dolor sit amet consectetur adipisicing elit. Natus sunt, odio, incidunt nulla quidem repellendus labore, maiores velit delectus earum numquam! Assumenda fuga voluptates expedita accusamus amet placeat veritatis voluptate.',
      date: new Date(),
      likedByUser: false,
      likes: Math.floor(Math.random() * 1_000),
      rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
    },
    {
      id: 1,
      playId: Math.floor(Math.random() * 10),
      author: {
        userId: 1,
        username: 'John Doe',
        profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
      },
      content:
        'Lorem ipsum dolor sit amet consectetur adipisicing elit. Natus sunt, odio, incidunt nulla quidem repellendus labore, maiores velit delectus earum numquam! Assumenda fuga voluptates expedita accusamus amet placeat veritatis voluptate.',
      date: new Date(),
      likedByUser: false,
      likes: Math.floor(Math.random() * 1_000),
      rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
    },
    {
      id: 1,
      playId: Math.floor(Math.random() * 10),
      author: {
        userId: 1,
        username: 'John Doe',
        profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
      },
      content:
        'Lorem ipsum dolor sit amet consectetur adipisicing elit. Natus sunt, odio, incidunt nulla quidem repellendus labore, maiores velit delectus earum numquam! Assumenda fuga voluptates expedita accusamus amet placeat veritatis voluptate.',
      date: new Date(),
      likedByUser: false,
      likes: Math.floor(Math.random() * 1_000),
      rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
    },
  ];
}
