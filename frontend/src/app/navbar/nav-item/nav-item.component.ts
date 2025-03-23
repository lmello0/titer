import { Component, Input } from '@angular/core';
import { NavItem } from '../interface/nav-item';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-nav-item',
  imports: [CommonModule, RouterLink],
  templateUrl: './nav-item.component.html',
  styleUrl: './nav-item.component.css',
})
export class NavItemComponent {
  @Input() item!: NavItem;

  onItemClick(event: MouseEvent): void {
    if (this.item.clickHandler) {
      this.item.clickHandler(event);
    }
  }
}
