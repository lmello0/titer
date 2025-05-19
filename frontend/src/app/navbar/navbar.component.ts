import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NavItem } from './nav-item-list/interface/nav-item.interface';
import { CommonModule } from '@angular/common';
import { NavLogoComponent } from './nav-logo/nav-logo.component';
import { NavItemListComponent } from './nav-item-list/nav-item-list.component';
import { MobileNavbarOpenService } from '../services/menu-open/mobile-navbar-open.service';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, NavLogoComponent, NavItemListComponent],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  logo: NavItem = { label: 'Títer', route: '#' };

  @Output() menuOpenEvent: EventEmitter<boolean> = new EventEmitter<boolean>();

  isSearching: boolean = false;
  isOnNavbarHover: boolean = false;
  isMouseOnLogo: boolean = false;

  constructor(public isMenuOpenService: MobileNavbarOpenService) {}

  toggleMenu(): void {
    this.isMenuOpenService.toggle();
    this.menuOpenEvent.emit(this.isMenuOpenService.currentValue);
  }

  closeMenu(): void {
    this.isMenuOpenService.set(false);
    this.menuOpenEvent.emit(this.isMenuOpenService.currentValue);
  }
}
