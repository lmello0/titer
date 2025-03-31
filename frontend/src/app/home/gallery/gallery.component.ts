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

  items: any[] = [];

  ngOnInit(): void {
    const urls: string[] = [
      'https://image.tmdb.org/t/p/w600_and_h900_bestv2/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg',
      'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/3bhkrj58Vtu7enYsRolD1fZdja1.jpg',
      'https://image.tmdb.org/t/p/w600_and_h900_bestv2/sF1U4EUQS8YHUYjNl3pMGNIQyr0.jpg',
      'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/qJ2tW6WMUDux911r6m7haRef0WH.jpg',
      'https://image.tmdb.org/t/p/w600_and_h900_bestv2/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg',
      'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg',
      'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/8VG8fDNiy50H4FedGwdSVUPoaJe.jpg',
      'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/vQWk5YBFWF4bZaofAbv0tShwBvQ.jpg',
      'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/q719jXXEzOoYaps6babgKnONONX.jpg',
      'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg',
    ];

    for (let i = 0; i < 5; i++) {
      const index = Math.floor(Math.random() * urls.length);
      const url = urls[index];

      urls.splice(index, 1);

      this.items.push({
        reviewCount: Math.floor(Math.random() * (1_000_000 * 10)),
        watchedCount: Math.floor(Math.random() * (1_000_000 * 10)),
        imageSrc: url,
      });
    }
  }
}
