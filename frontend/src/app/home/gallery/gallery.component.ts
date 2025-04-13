import { Component, Input, OnInit } from '@angular/core';
import { PlayCardComponent } from './play-card/play-card.component';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-gallery',
  imports: [CommonModule, PlayCardComponent, RouterLink],
  templateUrl: './gallery.component.html',
  styleUrl: './gallery.component.css',
})
export class GalleryComponent implements OnInit {
  @Input() galleryTitle: string = '';
  @Input() route: string = '';

  items: {
    reviewCount: number;
    watchedCount: number;
    title: string;
    imageSrc: string;
  }[] = [];

  ngOnInit(): void {
    const urls: { title: string; url: string }[] = [
      {
        title: 'The Shawshank Redemption',
        url: 'https://image.tmdb.org/t/p/w600_and_h900_bestv2/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg',
      },
      {
        title: 'The Godfather',
        url: 'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/3bhkrj58Vtu7enYsRolD1fZdja1.jpg',
      },
      {
        title: "Schindler's List",
        url: 'https://image.tmdb.org/t/p/w600_and_h900_bestv2/sF1U4EUQS8YHUYjNl3pMGNIQyr0.jpg',
      },
      {
        title: 'The Dark Knight',
        url: 'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/qJ2tW6WMUDux911r6m7haRef0WH.jpg',
      },
      {
        title: 'Spirited Away',
        url: 'https://image.tmdb.org/t/p/w600_and_h900_bestv2/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg',
      },
      {
        title: 'Parasite',
        url: 'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg',
      },
      {
        title: 'The Green Mile',
        url: 'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/8VG8fDNiy50H4FedGwdSVUPoaJe.jpg',
      },
      {
        title: 'Pulp Fiction',
        url: 'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/vQWk5YBFWF4bZaofAbv0tShwBvQ.jpg',
      },
      {
        title: 'Your Name',
        url: 'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/q719jXXEzOoYaps6babgKnONONX.jpg',
      },
      {
        title: 'The Lord of the Rings: The return of the king',
        url: 'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg',
      },
    ];

    for (let i = 0; i < 5; i++) {
      const index = Math.floor(Math.random() * urls.length);
      const url = urls[index];

      urls.splice(index, 1);

      this.items.push({
        reviewCount: Math.floor(Math.random() * (1_000_000 * 10)),
        watchedCount: Math.floor(Math.random() * (1_000_000 * 10)),
        title: url.title,
        imageSrc: url.url,
      });
    }
  }
}
