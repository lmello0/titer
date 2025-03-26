import { Component } from '@angular/core';
import { TrendingComponent } from './trending/trending.component';
import { NewPlaysComponent } from './new-plays/new-plays.component';
import { MostPopularAllTimeComponent } from './most-popular-all-time/most-popular-all-time.component';

@Component({
  selector: 'app-home',
  imports: [TrendingComponent, NewPlaysComponent, MostPopularAllTimeComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {}
