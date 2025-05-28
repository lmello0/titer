import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NavItem } from '../interface/nav-item.interface';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-nav-item',
  imports: [CommonModule, RouterLink],
  templateUrl: './nav-item-link.component.html',
  styleUrls: ['./nav-item-link.component.css', '../../navbar.component.css'],
})
export class NavItemLinkComponent implements NavItem {
  @Input() label!: string;
  @Input() route?: string | any[] | undefined;
  @Input() isOnNavbarHover: boolean = false;

  @Output() onClick = new EventEmitter<string>();

  isMouseOn: boolean = false;
}
