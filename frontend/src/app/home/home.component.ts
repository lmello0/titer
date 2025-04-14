import { Component } from '@angular/core';
import { TrendingComponent } from './trending/trending.component';
import { SpotlightComponent } from './spotlight/spotlight.component';
import { FeaturesComponent } from './features/features.component';
import { LastReviewsComponent } from './last-reviews/last-reviews.component';
import { FooterComponent } from '../footer/footer.component';

@Component({
  selector: 'app-home',
  imports: [
    TrendingComponent,
    SpotlightComponent,
    FeaturesComponent,
    LastReviewsComponent,
    FooterComponent,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {}
