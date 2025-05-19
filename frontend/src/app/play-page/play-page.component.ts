import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PlayPosterComponent } from './play-poster/play-poster.component';
import { PlayTitleComponent } from './play-title/play-title.component';
import { TabContentComponent } from './tab-content/tab-content.component';
import { CommentSectionComponent } from './comment-section/comment-section.component';
import { MobileNavbarOpenService } from '../services/menu-open/mobile-navbar-open.service';
import { PlayDetails } from '../shared/interfaces/play-details.interface';

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
  playId!: string;

  constructor(
    private route: ActivatedRoute,
    public isMenuOpenService: MobileNavbarOpenService,
  ) {}

  ngOnInit(): void {
    this.playId = this.route.snapshot.paramMap.get('id')!;
  }

  play: PlayDetails = {
    playId: '1',
    title: 'Spirited Away',
    director: { directorId: '1', directorName: 'Hayao Miyazaki' },
    synopsis:
      'Lorem ipsum dolor sit amet consectetur adipisicing elit. Incidunt quae ad omnis qui iusto sapiente, vel fugiat id magni, dolore est corrupti neque eveniet! Minima modi eligendi natus explicabo sapiente.',
    releaseYear: 2001,
    rating: 1 + Math.round(Math.random() * 5 * 10) / 10,
    watchCount: Math.floor(Math.random() * (1_000_000 * 10)),
    reviewCount: Math.floor(Math.random() * (1_000_000 * 10)),
    posterImageUrl:
      'https://image.tmdb.org/t/p/w600_and_h900_bestv2/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg',
    directors: [
      { directorId: '1', directorName: 'Director 1' },
      { directorId: '2', directorName: 'Director 2' },
    ],
    mainActors: [
      { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
      { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
      { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
      { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
    ],
    genres: [
      { genreId: '1', genreName: 'Genre 1' },
      { genreId: '2', genreName: 'Genre 2' },
      { genreId: '3', genreName: 'Genre 3' },
    ],
    duration: 30 + Math.floor(Math.random() * 120),
    premiereDate: new Date(),
    premiereTheatre: { theatreId: '1', theatreName: 'Theatre' },
    cast: [
      { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
      { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
      { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
      { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
      { actorId: '5', actorName: 'Actor 5', character: 'Character 5' },
      { actorId: '6', actorName: 'Actor 6', character: 'Character 6' },
      { actorId: '7', actorName: 'Actor 7', character: 'Character 7' },
      { actorId: '8', actorName: 'Actor 8', character: 'Character 8' },
      { actorId: '9', actorName: 'Actor 9', character: 'Character 9' },
      { actorId: '10', actorName: 'Actor 10', character: 'Character 10' },
    ],
    crew: [
      { workerId: '1', workerName: 'Crew 1', role: 'Role 1' },
      { workerId: '2', workerName: 'Crew 2', role: 'Role 2' },
      { workerId: '3', workerName: 'Crew 3', role: 'Role 3' },
      { workerId: '4', workerName: 'Crew 4', role: 'Role 4' },
      { workerId: '5', workerName: 'Crew 5', role: 'Role 5' },
      { workerId: '6', workerName: 'Crew 6', role: 'Role 6' },
      { workerId: '7', workerName: 'Crew 7', role: 'Role 7' },
      { workerId: '8', workerName: 'Crew 8', role: 'Role 8' },
      { workerId: '9', workerName: 'Crew 9', role: 'Role 9' },
      { workerId: '10', workerName: 'Crew 10', role: 'Role 10' },
    ],
    releases: [
      {
        releaseDate: new Date('2025-05-13'),
        theatres: [
          { theatreId: '1', theatreName: 'Theatre 1' },
          { theatreId: '2', theatreName: 'Theatre 2' },
        ],
      },
      {
        releaseDate: new Date('2025-05-12'),
        theatres: [
          { theatreId: '3', theatreName: 'Theatre 3' },
          { theatreId: '4', theatreName: 'Theatre 4' },
          { theatreId: '5', theatreName: 'Theatre 5' },
        ],
      },
      {
        releaseDate: new Date('2025-05-11'),
        theatres: [{ theatreId: '6', theatreName: 'Theatre 6' }],
      },
    ],
    socials: [
      {
        platform: 'instagram',
        socialIconUrl: '',
        socialLink: 'https://url.com',
        socialName: 'miyazaki_hayao',
      },
    ],
    comments: [
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
    ],
  };
}
