import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-nav-logo',
  imports: [CommonModule],
  templateUrl: './nav-logo.component.html',
  styleUrls: ['./nav-logo.component.css', '../navbar.component.css'],
})
export class NavLogoComponent {
  @Input() isOnNavbarHover: boolean = false;
  @Output() menuClosed = new EventEmitter<void>();

  isMouseOnLogo: boolean = false;

  closeMenu() {
    this.menuClosed.emit();
  }

  changeMouseOnLogo(value: boolean) {
    this.isMouseOnLogo = value;
  }
}
