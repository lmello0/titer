import { Injectable } from '@angular/core';
import { Genre } from '../../shared/interfaces/genre.interface';
import { delay, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class GenreService {
  private mockGenres: Genre[] = [
    { genreId: 'g1', genreName: 'Drama' },
    { genreId: 'g2', genreName: 'Comedy' },
    { genreId: 'g3', genreName: 'Musical' },
    { genreId: 'g4', genreName: 'Tragedy' },
    { genreId: 'g5', genreName: 'Experimental' },
    { genreId: 'g6', genreName: 'Other' },
  ];

  constructor() {}

  public getAllGenres(): Observable<Genre[]> {
    return of(this.mockGenres).pipe(delay(1_000));
  }
}
