import { Injectable } from '@angular/core';
import { Actor } from '../../shared/interfaces/actor.interface';
import { delay, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ActorService {
  private mockActors: Actor[] = [
    { actorId: '1', actorName: 'Tom Cruise' },
    { actorId: '2', actorName: 'Ana de Armas' },
    { actorId: '3', actorName: 'Tramel Tillman' },
    { actorId: '4', actorName: 'Natalie Portman' },
  ];

  constructor() {}

  public getAllActors(): Observable<Actor[]> {
    return of(this.mockActors).pipe(delay(2_000));
  }
}
