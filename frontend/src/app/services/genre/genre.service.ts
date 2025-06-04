import { Injectable } from '@angular/core';
import { Genre } from '../../shared/interfaces/genre.interface';
import { delay, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class GenreService {
  private mockGenres: Genre[] = [
    { genreId: '1', genreName: 'Action' },
    { genreId: '2', genreName: 'Drama' },
    { genreId: '3', genreName: 'Comedy' },
  ];

  constructor() {}

  public getAllGenres(): Observable<Genre[]> {
    return of(this.mockGenres).pipe(delay(1_000));
  }
}
