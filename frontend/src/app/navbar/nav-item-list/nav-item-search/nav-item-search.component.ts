import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-nav-item-search',
  imports: [CommonModule, FormsModule],
  templateUrl: './nav-item-search.component.html',
  styleUrl: './nav-item-search.component.css',
})
export class NavItemSearchComponent {
  @Input() isOnNavbarHover: boolean = false;

  isSearching: boolean = false;
  playToSearch: string = '';

  get getBackgroundColor(): string {
    if (this.isSearching || this.isOnNavbarHover) {
      return 'bg-gray-200';
    }

    if (!this.isSearching && !this.isOnNavbarHover) {
      return 'bg-gray-700';
    }

    return '';
  }

  changeIsSearchingStatus(value: boolean) {
    this.isSearching = value;
  }

  onSubmit(): void {
    // TODO: Add search logic
  }
}
