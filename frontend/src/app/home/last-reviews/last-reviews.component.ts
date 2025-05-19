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
      play: {
        title: 'The Shawshank Redemption',
        posterUrl:
          'https://image.tmdb.org/t/p/w600_and_h900_bestv2/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg',
        date: {
          fullDate: new Date(),
          day: 1,
          month: 1,
          year:
            Math.floor(Math.random() * (new Date().getFullYear() - 1930 + 1)) +
            1930,
        },
      },
      user: {
        name: 'User',
        avatarUrl:
          'https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_640.png',
      },
      reviewText:
        'Lorem ipsum dolor sit amet consectetur adipisicing elit. Natus sunt, odio, incidunt nulla quidem repellendus labore, maiores velit delectus earum numquam! Assumenda fuga voluptates expedita accusamus amet placeat veritatis voluptate.',
      rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
      numLikes: Math.floor(Math.random() * (10_000 - 1_00 + 1)) + 1_00,
      numComments: Math.floor(Math.random() * (10_000 - 1_00 + 1)) + 1_00,
    },
    {
      play: {
        title: 'The Shawshank Redemption',
        posterUrl:
          'https://image.tmdb.org/t/p/w600_and_h900_bestv2/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg',
        date: {
          fullDate: new Date(),
          day: 1,
          month: 1,
          year:
            Math.floor(Math.random() * (new Date().getFullYear() - 1930 + 1)) +
            1930,
        },
      },
      user: {
        name: 'User',
        avatarUrl:
          'https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_640.png',
      },
      reviewText:
        'Lorem ipsum dolor sit amet consectetur adipisicing elit. Natus sunt, odio, incidunt nulla quidem repellendus labore, maiores velit delectus earum numquam! Assumenda fuga voluptates expedita accusamus amet placeat veritatis voluptate.',
      rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
      numLikes: Math.floor(Math.random() * (10_000 - 1_00 + 1)) + 1_00,
      numComments: Math.floor(Math.random() * (10_000 - 1_00 + 1)) + 1_00,
    },
    {
      play: {
        title: 'The Shawshank Redemption',
        posterUrl:
          'https://image.tmdb.org/t/p/w600_and_h900_bestv2/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg',
        date: {
          fullDate: new Date(),
          day: 1,
          month: 1,
          year:
            Math.floor(Math.random() * (new Date().getFullYear() - 1930 + 1)) +
            1930,
        },
      },
      user: {
        name: 'User',
        avatarUrl:
          'https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_640.png',
      },
      reviewText:
        'Lorem ipsum dolor sit amet consectetur adipisicing elit. Natus sunt, odio, incidunt nulla quidem repellendus labore, maiores velit delectus earum numquam! Assumenda fuga voluptates expedita accusamus amet placeat veritatis voluptate.',
      rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
      numLikes: Math.floor(Math.random() * (10_000 - 1_00 + 1)) + 1_00,
      numComments: Math.floor(Math.random() * (10_000 - 1_00 + 1)) + 1_00,
    },
    {
      play: {
        title: 'The Shawshank Redemption',
        posterUrl:
          'https://image.tmdb.org/t/p/w600_and_h900_bestv2/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg',
        date: {
          fullDate: new Date(),
          day: 1,
          month: 1,
          year:
            Math.floor(Math.random() * (new Date().getFullYear() - 1930 + 1)) +
            1930,
        },
      },
      user: {
        name: 'User',
        avatarUrl:
          'https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_640.png',
      },
      reviewText:
        'Lorem ipsum dolor sit amet consectetur adipisicing elit. Natus sunt, odio, incidunt nulla quidem repellendus labore, maiores velit delectus earum numquam! Assumenda fuga voluptates expedita accusamus amet placeat veritatis voluptate.',
      rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
      numLikes: Math.floor(Math.random() * (10_000 - 1_00 + 1)) + 1_00,
      numComments: Math.floor(Math.random() * (10_000 - 1_00 + 1)) + 1_00,
    },
    {
      play: {
        title: 'The Shawshank Redemption',
        posterUrl:
          'https://image.tmdb.org/t/p/w600_and_h900_bestv2/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg',
        date: {
          fullDate: new Date(),
          day: 1,
          month: 1,
          year:
            Math.floor(Math.random() * (new Date().getFullYear() - 1930 + 1)) +
            1930,
        },
      },
      user: {
        name: 'User',
        avatarUrl:
          'https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_640.png',
      },
      reviewText:
        'Lorem ipsum dolor sit amet consectetur adipisicing elit. Natus sunt, odio, incidunt nulla quidem repellendus labore, maiores velit delectus earum numquam! Assumenda fuga voluptates expedita accusamus amet placeat veritatis voluptate.',
      rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
      numLikes: Math.floor(Math.random() * (10_000 - 1_00 + 1)) + 1_00,
      numComments: Math.floor(Math.random() * (10_000 - 1_00 + 1)) + 1_00,
    },
    {
      play: {
        title: 'The Shawshank Redemption',
        posterUrl:
          'https://image.tmdb.org/t/p/w600_and_h900_bestv2/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg',
        date: {
          fullDate: new Date(),
          day: 1,
          month: 1,
          year:
            Math.floor(Math.random() * (new Date().getFullYear() - 1930 + 1)) +
            1930,
        },
      },
      user: {
        name: 'User',
        avatarUrl:
          'https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_640.png',
      },
      reviewText:
        'Lorem ipsum dolor sit amet consectetur adipisicing elit. Natus sunt, odio, incidunt nulla quidem repellendus labore, maiores velit delectus earum numquam! Assumenda fuga voluptates expedita accusamus amet placeat veritatis voluptate.',
      rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
      numLikes: Math.floor(Math.random() * (10_000 - 1_00 + 1)) + 1_00,
      numComments: Math.floor(Math.random() * (10_000 - 1_00 + 1)) + 1_00,
    },
  ];
}
