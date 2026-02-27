import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { PlayPageComponent } from './play-page/play-page.component';
import { SubmitNewPlayComponent } from './submit-new-play/submit-new-play.component';
import { EntityPlaceholderComponent } from './entity-placeholder/entity-placeholder.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'play/:id', component: PlayPageComponent },
  { path: 'submit-new-play', component: SubmitNewPlayComponent },
  { path: 'list/:type', component: EntityPlaceholderComponent },
  { path: 'director/:id', component: EntityPlaceholderComponent },
  { path: 'actor/:id', component: EntityPlaceholderComponent },
  { path: 'genre/:id', component: EntityPlaceholderComponent },
  { path: 'theatre/:id', component: EntityPlaceholderComponent },
  { path: 'worker/:id', component: EntityPlaceholderComponent },
  { path: 'plays/:year', component: EntityPlaceholderComponent },
  { path: '**', redirectTo: '' },
];
