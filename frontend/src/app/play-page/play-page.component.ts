import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Comment } from './interface/comment';
import { PlayPosterComponent } from './play-poster/play-poster.component';
import { PlayTitleComponent } from './play-title/play-title.component';
import { TabContentComponent } from './tab-content/tab-content.component';
import { CommentSectionComponent } from './comment-section/comment-section.component';
import { MobileNavbarOpenService } from '../../services/menu-open/mobile-navbar-open.service';

@Component({
  selector: 'app-play-page',
  imports: [
    CommonModule,
    PlayPosterComponent,
    PlayTitleComponent,
    TabContentComponent,
    CommentSectionComponent,
  ],
  templateUrl: './play-page.component.html',
  styleUrl: './play-page.component.css',
})
export class PlayPageComponent implements OnInit {
  title: string = 'Spirited Away';
  director: string = 'Hayao Miyazaki';
  year: number = 2001;
  rating: number = 1 + Math.round(Math.random() * 5 * 10) / 10;
  watchCount: number = Math.floor(Math.random() * (1_000_000 * 10));
  reviewCount: number = Math.floor(Math.random() * (1_000_000 * 10));
  url: string =
    'https://image.tmdb.org/t/p/w600_and_h900_bestv2/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg';

  playId!: string;
  synopsis: string =
    'Lorem ipsum dolor sit amet consectetur adipisicing elit. Incidunt quae ad omnis qui iusto sapiente, vel fugiat id magni, dolore est corrupti neque eveniet! Minima modi eligendi natus explicabo sapiente.';

  tabs: string[] = ['MAIN INFO', 'CAST', 'CREW', 'RELEASES'];
  activeTab: string = this.tabs[0];

  directors: any[] = [
    { id: 1, name: 'Director 1' },
    { id: 2, name: 'Director 2' },
  ];

  mainActors: any[] = [
    { id: 1, name: 'Actor 1' },
    { id: 2, name: 'Actor 2' },
    { id: 3, name: 'Actor 3' },
    { id: 4, name: 'Actor 4' },
  ];

  genres: any[] = [
    { id: 1, name: 'Genre 1' },
    { id: 2, name: 'Genre 2' },
    { id: 3, name: 'Genre 3' },
  ];

  duration: number = 30 + Math.floor(Math.random() * 120);

  premiereDate: Date = new Date();
  premiereTheatre: { id: number; name: string } = { id: 1, name: 'Theatre' };

  cast: { id: number; name: string; character: string }[] = [
    { id: 1, name: 'Actor 1', character: 'Character 1' },
    { id: 2, name: 'Actor 2', character: 'Character 2' },
    { id: 3, name: 'Actor 3', character: 'Character 3' },
    { id: 4, name: 'Actor 4', character: 'Character 4' },
    { id: 5, name: 'Actor 5', character: 'Character 5' },
    { id: 6, name: 'Actor 6', character: 'Character 6' },
    { id: 7, name: 'Actor 7', character: 'Character 7' },
    { id: 8, name: 'Actor 8', character: 'Character 8' },
    { id: 9, name: 'Actor 9', character: 'Character 9' },
    { id: 10, name: 'Actor 10', character: 'Character 10' },
  ];

  crew: { id: number; name: string; role: string }[] = [
    { id: 1, name: 'Crew 1', role: 'Role 1' },
    { id: 2, name: 'Crew 2', role: 'Role 2' },
    { id: 3, name: 'Crew 3', role: 'Role 3' },
    { id: 4, name: 'Crew 4', role: 'Role 4' },
    { id: 5, name: 'Crew 5', role: 'Role 5' },
    { id: 6, name: 'Crew 6', role: 'Role 6' },
    { id: 7, name: 'Crew 7', role: 'Role 7' },
    { id: 8, name: 'Crew 8', role: 'Role 8' },
    { id: 9, name: 'Crew 9', role: 'Role 9' },
    { id: 10, name: 'Crew 10', role: 'Role 10' },
  ];

  releases: { date: string; names: string[] }[] = [
    { date: '13/05/2025', names: ['Theatre 1', 'Theatre 2'] },
    { date: '12/05/2025', names: ['Theatre 3', 'Theatre 4', 'Theatre 5'] },
    { date: '11/05/2025', names: ['Theatre 6'] },
  ];

  socials: {
    social: string;
    socialIcon: string | null | undefined;
    url: string;
    name: string;
  }[] = [
    {
      social: 'instagram',
      socialIcon: null,
      url: 'https://url.com',
      name: 'miyazaki_hayao',
    },
  ];

  comments: Comment[] = [
    {
      id: 1,
      author: 'Jane Doe',
      profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
      date: new Date(),
      content: 'This is such a helpful post! Thanks for sharing',
      likes: Math.floor(Math.random() * 100),
      likedByUser: false,
    },
    {
      id: 2,
      author: 'John Smith',
      profilePhotoUrl: 'https://i.pravatar.cc/150?img=3',
      date: new Date(),
      content: 'I disagree with your take, but interesting perspective!',
      likes: Math.floor(Math.random() * 100),
      likedByUser: false,
    },
  ];

  constructor(
    private route: ActivatedRoute,
    public isMenuOpenService: MobileNavbarOpenService,
  ) {}

  ngOnInit(): void {
    this.playId = this.route.snapshot.paramMap.get('id')!;
  }

  selectTab(tab: string): void {
    this.activeTab = tab;
  }
}
