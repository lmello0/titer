import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { NavItem } from './interface/nav-item.interface';
import { NavItemLinkComponent } from './nav-item-link/nav-item-link.component';
import { NavItemSearchComponent } from './nav-item-search/nav-item-search.component';

@Component({
  selector: 'app-nav-item-list',
  imports: [CommonModule, NavItemLinkComponent, NavItemSearchComponent],
  templateUrl: './nav-item-list.component.html',
  styleUrl: './nav-item-list.component.css',
})
export class NavItemListComponent {
  @Input() isOnNavbarHover: boolean = false;
  @Input() isMenuOpen: boolean = false;

  isSearching: boolean = false;

  navItems: NavItem[] = [
    { label: 'Plays', route: '#' },
    { label: 'Lists', route: '#' },
    { label: 'Sign In', route: '#' },
    { label: 'Create Account', route: '#' },
  ];
}
