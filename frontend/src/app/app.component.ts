import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './navbar/navbar.component';
import { NgClass } from '@angular/common';
import { FooterComponent } from './footer/footer.component';
import { MobileNavbarOpenService } from '../services/menu-open/mobile-navbar-open.service';

@Component({
  selector: 'app-root',
  imports: [NavbarComponent, NgClass, RouterOutlet, FooterComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'titer';

  constructor(public isMenuOpenService: MobileNavbarOpenService) {}

  toggleMenu(): void {
    this.isMenuOpenService.toggle();
  }
}
