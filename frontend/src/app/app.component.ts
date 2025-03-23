import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './navbar/navbar.component';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [NavbarComponent, NgClass, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'titer';

  isMenuOpen: boolean = false;

  menuOpenEventReceiver(event: boolean): void {
    this.isMenuOpen = event;
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }
}
