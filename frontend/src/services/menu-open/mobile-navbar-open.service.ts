import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class MobileNavbarOpenService {
  private isMenuOpenSubject = new BehaviorSubject<boolean>(false);
  private isMenuOpen$ = this.isMenuOpenSubject.asObservable();

  toggle(): void {
    this.isMenuOpenSubject.next(!this.isMenuOpenSubject.value);
  }

  set(value: boolean): void {
    this.isMenuOpenSubject.next(value);
  }

  get currentValue(): boolean {
    return this.isMenuOpenSubject.value;
  }
}
