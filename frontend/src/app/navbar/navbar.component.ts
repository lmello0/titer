import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NavItem } from './nav-item-list/interface/nav-item.interface';
import { CommonModule } from '@angular/common';
import { NavLogoComponent } from './nav-logo/nav-logo.component';
import { NavItemListComponent } from './nav-item-list/nav-item-list.component';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, NavLogoComponent, NavItemListComponent],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  logo: NavItem = { label: 'Títer', route: '#' };

  @Input() isMenuOpen: boolean = false;
  @Output() menuOpenEvent: EventEmitter<boolean> = new EventEmitter<boolean>();

  isSearching: boolean = false;
  isOnNavbarHover: boolean = false;
  isMouseOnLogo: boolean = false;

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
    this.menuOpenEvent.emit(this.isMenuOpen);
  }

  closeMenu(): void {
    this.isMenuOpen = false;
    this.menuOpenEvent.emit(this.isMenuOpen);
  }
}
