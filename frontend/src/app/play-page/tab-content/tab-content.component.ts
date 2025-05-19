import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TabMainInfoComponent } from './tab-main-info/tab-main-info.component';
import { TabCastComponent } from './tab-cast/tab-cast.component';
import { TabCrewComponent } from './tab-crew/tab-crew.component';
import { TabReleasesComponent } from './tab-releases/tab-releases.component';
import { Theatre } from '../../shared/interfaces/theatre';
import { Release } from '../../shared/interfaces/release';
import { Actor } from '../../shared/interfaces/actor';
import { Worker } from '../../shared/interfaces/worker';
import { Director } from '../../shared/interfaces/director';
import { Genre } from '../../shared/interfaces/genre';
import { Social } from '../../shared/interfaces/social';

@Component({
  selector: 'app-tab-content',
  imports: [
    CommonModule,
    TabMainInfoComponent,
    TabCastComponent,
    TabCrewComponent,
    TabReleasesComponent,
  ],
  templateUrl: './tab-content.component.html',
  styleUrl: './tab-content.component.css',
})
export class TabContentComponent {
  @Input() premiereDate!: Date;
  @Input() premiereTheatre!: Theatre;
  @Input() duration!: number;

  @Input() releases!: Release[];
  @Input() cast!: Actor[];
  @Input() crew!: Worker[];
  @Input() directors!: Director[];
  @Input() mainActors!: Actor[];
  @Input() genres!: Genre[];
  @Input() socials!: Social[];

  tabs: string[] = ['MAIN INFO', 'CAST', 'CREW', 'RELEASES'];
  activeTab: string = this.tabs[0];

  selectTab(tab: string): void {
    this.activeTab = tab;
  }
}
