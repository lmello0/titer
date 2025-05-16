import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TabMainInfoComponent } from './tab-main-info/tab-main-info.component';
import { TabCastComponent } from './tab-cast/tab-cast.component';
import { TabCrewComponent } from './tab-crew/tab-crew.component';
import { TabReleasesComponent } from './tab-releases/tab-releases.component';

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
  @Input() premiereTheatre!: { id: number; name: string };
  @Input() duration!: number;

  @Input() releases!: { date: string; names: string[] }[];
  @Input() cast!: { id: number; name: string; character: string }[];
  @Input() crew!: { id: number; name: string; role: string }[];
  @Input() directors!: { id: number; name: string }[];
  @Input() mainActors!: { id: number; name: string }[];
  @Input() genres!: { id: number; name: string }[];
  @Input() socials!: {
    social: string;
    socialIcon: string | null | undefined;
    url: string;
    name: string;
  }[];

  tabs: string[] = ['MAIN INFO', 'CAST', 'CREW', 'RELEASES'];
  activeTab: string = this.tabs[0];

  selectTab(tab: string): void {
    this.activeTab = tab;
  }
}
