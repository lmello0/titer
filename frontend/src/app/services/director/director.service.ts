import { Injectable } from '@angular/core';
import { Director } from '../../shared/interfaces/director.interface';
import { delay, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DirectorService {
  private mockDirectors: Director[] = [
    { directorId: '1', directorName: 'Greta Gerwig' },
    { directorId: '2', directorName: 'Quentin Tarantino' },
    { directorId: '3', directorName: 'Martin Scorsese' },
    { directorId: '4', directorName: 'Christopher Nolan' },
    { directorId: '5', directorName: 'Francis Ford Coppola' },
    { directorId: '6', directorName: 'Frank Darabont' },
  ];

  constructor() {}

  public getAllDirectors(): Observable<Director[]> {
    return of(this.mockDirectors).pipe(delay(1_000));
  }
}
