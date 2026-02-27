import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-nav-logo',
  imports: [CommonModule, RouterLink],
  templateUrl: './nav-logo.component.html',
  styleUrls: ['./nav-logo.component.css', '../navbar.component.css'],
})
export class NavLogoComponent {
  @Input() isOnNavbarHover: boolean = false;
}
