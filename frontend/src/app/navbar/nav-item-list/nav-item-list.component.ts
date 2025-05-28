import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NavItem } from './interface/nav-item.interface';
import { NavItemLinkComponent } from './nav-item-link/nav-item-link.component';
import { NavItemSearchComponent } from './nav-item-search/nav-item-search.component';
import { MobileNavbarOpenService } from '../../services/menu-open/mobile-navbar-open.service';

@Component({
  selector: 'app-nav-item-list',
  imports: [CommonModule, NavItemLinkComponent, NavItemSearchComponent],
  templateUrl: './nav-item-list.component.html',
  styleUrl: './nav-item-list.component.css',
})
export class NavItemListComponent {
  @Input() isOnNavbarHover: boolean = false;
  @Output() onItemClick = new EventEmitter<string>();

  isSearching: boolean = false;

  navItems: NavItem[] = [
    { label: 'Sign In', route: '#' },
    { label: 'Create Account', route: '#' },
  ];

  constructor(public isMenuOpenService: MobileNavbarOpenService) {}

  click(label: string) {
    this.onItemClick.emit(label);
  }
}
