import { Component } from '@angular/core';
import { TrendingComponent } from './trending/trending.component';

@Component({
  selector: 'app-home',
  imports: [TrendingComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {}
