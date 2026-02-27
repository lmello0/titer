import { Injectable } from '@angular/core';
import { Director } from '../../shared/interfaces/director.interface';
import { delay, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DirectorService {
  private mockDirectors: Director[] = [
    { directorId: 'd1', directorName: 'Sam Mendes' },
    { directorId: 'd2', directorName: 'Kenny Leon' },
    { directorId: 'd3', directorName: 'Yael Farber' },
    { directorId: 'd4', directorName: 'Phylicia Rashad' },
    { directorId: 'd5', directorName: 'Jamie Lloyd' },
    { directorId: 'd6', directorName: 'Ivo van Hove' },
  ];

  constructor() {}

  public getAllDirectors(): Observable<Director[]> {
    return of(this.mockDirectors).pipe(delay(1_000));
  }
}
