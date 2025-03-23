import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NavItem } from './interface/nav-item';
import { CommonModule, NgClass } from '@angular/common';
import { NavItemComponent } from './nav-item/nav-item.component';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, NavItemComponent],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  logo: NavItem = { label: 'Títer', route: '#' };
  navItems: NavItem[] = [
    { label: 'Sign In', route: '#' },
    { label: 'Create Account', route: '#' },
  ];

  @Input() isMenuOpen: boolean = false;
  @Output() menuOpenEvent: EventEmitter<boolean> = new EventEmitter<boolean>();

  isSearching: boolean = false;
  isOnNavbarHover: boolean = false;
  isMouseOnLogo: boolean = false;

  constructor() {
    this.prepareNavItems();
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
    this.menuOpenEvent.emit(this.isMenuOpen);
  }

  closeMenu(): void {
    this.isMenuOpen = false;
    this.menuOpenEvent.emit(this.isMenuOpen);
  }

  private prepareNavItems(): void {
    const navItems = this.navItems.map((item, index) => ({
      ...item,
      position: item.position ?? index,
    }));

    const sortedNavItems = [...navItems].sort(
      (a, b) => a.position - b.position,
    );

    if (sortedNavItems.length > 0) {
      sortedNavItems.forEach((item) => (item.rightest = false));
      sortedNavItems[sortedNavItems.length - 1].rightest = true;
    }

    this.navItems = sortedNavItems;
  }
}
