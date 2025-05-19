import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TabMainInfoComponent } from './tab-main-info/tab-main-info.component';
import { TabCastComponent } from './tab-cast/tab-cast.component';
import { TabCrewComponent } from './tab-crew/tab-crew.component';
import { TabReleasesComponent } from './tab-releases/tab-releases.component';
import { Theatre } from '../../shared/interfaces/theatre.interface';
import { Release } from '../../shared/interfaces/release.interface';
import { Actor } from '../../shared/interfaces/actor.interface';
import { Worker } from '../../shared/interfaces/worker.interface';
import { Director } from '../../shared/interfaces/director.interface';
import { Genre } from '../../shared/interfaces/genre.interface';
import { Social } from '../../shared/interfaces/social.interface';

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
  @Input() director!: Director;
  @Input() mainActors!: Actor[];
  @Input() genres!: Genre[];
  @Input() socials!: Social[];

  tabs: string[] = ['MAIN INFO', 'CAST', 'CREW', 'RELEASES'];
  activeTab: string = this.tabs[0];

  selectTab(tab: string): void {
    this.activeTab = tab;
  }
}
