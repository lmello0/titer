import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nav-item-search',
  imports: [CommonModule, FormsModule],
  templateUrl: './nav-item-search.component.html',
  styleUrl: './nav-item-search.component.css',
})
export class NavItemSearchComponent {
  @Input() isOnNavbarHover = false;

  isSearching = false;
  playToSearch = '';

  constructor(private readonly router: Router) {}

  changeIsSearchingStatus(value: boolean): void {
    this.isSearching = value;
  }

  onSubmit(): void {
    const searchTerm = this.playToSearch.trim();

    if (!searchTerm) {
      return;
    }

    this.router.navigate(['/list/search-results'], { queryParams: { q: searchTerm } });
  }
}
