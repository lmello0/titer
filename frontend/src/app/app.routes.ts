import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { PlayPageComponent } from './play-page/play-page.component';
import { SubmitNewPlayComponent } from './submit-new-play/submit-new-play.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'play/:id', component: PlayPageComponent },
  { path: 'submit-new-play', component: SubmitNewPlayComponent },
];
