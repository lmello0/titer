import { Component, Input } from '@angular/core';
import { NavItem } from '../interface/nav-item';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-nav-item',
  imports: [CommonModule, RouterLink],
  templateUrl: './nav-item.component.html',
  styleUrls: ['./nav-item.component.css', '../navbar.component.css'],
})
export class NavItemComponent {
  @Input() item!: NavItem;
  @Input() isOnNavbarHover: boolean = false;

  isMouseOn: boolean = false;

  onItemClick(event: MouseEvent): void {
    if (this.item.clickHandler) {
      this.item.clickHandler(event);
    }
  }
}
