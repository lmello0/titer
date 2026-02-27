import { Injectable } from '@angular/core';
import { Actor } from '../../shared/interfaces/actor.interface';
import { delay, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ActorService {
  private mockActors: Actor[] = [
    { actorId: 'a1', actorName: 'David Oyelowo' },
    { actorId: 'a2', actorName: 'Ruth Wilson' },
    { actorId: 'a3', actorName: 'Mark Rylance' },
    { actorId: 'a4', actorName: 'Wendell Pierce' },
  ];

  constructor() {}

  public getAllActors(): Observable<Actor[]> {
    return of(this.mockActors).pipe(delay(2_000));
  }
}
