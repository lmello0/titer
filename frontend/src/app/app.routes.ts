import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { PlayPageComponent } from './play-page/play-page.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'play/:id', component: PlayPageComponent },
];
