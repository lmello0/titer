import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-play-page',
  imports: [RouterLink, CommonModule],
  templateUrl: './play-page.component.html',
  styleUrl: './play-page.component.css',
})
export class PlayPageComponent implements OnInit {
  formatter = Intl.NumberFormat('en', { notation: 'compact' });

  title: string = 'Spirited Away';
  director: string = 'Hayao Miyazaki';
  year: number = 2001;
  rating: number = 1 + Math.round(Math.random() * 5 * 10) / 10;
  watchCount: string = this.formatter.format(
    Math.floor(Math.random() * (1_000_000 * 10)),
  );
  reviewCount: string = this.formatter.format(
    Math.floor(Math.random() * (1_000_000 * 10)),
  );
  url: string =
    'https://image.tmdb.org/t/p/w600_and_h900_bestv2/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg';

  playId!: string;

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

  currDate: Date = new Date();

  premiereDate: string = this.getDate();
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

  releases: any = {
    '13/05/2025': [
      { id: 1, name: 'Theatre 1' },
      { id: 2, name: 'Theatre 2' },
    ],
    '12/05/2025': [
      { id: 3, name: 'Theatre 3' },
      { id: 4, name: 'Theatre 4' },
      { id: 5, name: 'Theatre 5' },
    ],
    '11/05/2025': [{ id: 6, name: 'Theatre 6' }],
  };

  sortedReleases: { date: string; names: string[] }[] = [];

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.playId = this.route.snapshot.paramMap.get('id')!;

    const dates = Object.keys(this.releases).sort((a, b) => {
      const [da, ma, ya] = a.split('/').map(Number);
      const [db, mb, yb] = b.split('/').map(Number);

      const dateA = new Date(ya, ma - 1, da);
      const dateB = new Date(yb, mb - 1, db);

      return dateB.getTime() - dateA.getTime();
    });

    this.sortedReleases = dates.map((date) => ({
      date,
      names: this.releases[date].map((t: { name: any }) => t.name),
    }));
  }

  selectTab(tab: string): void {
    this.activeTab = tab;
  }

  private getDate(): string {
    const today = new Date();

    const day = String(today.getDate()).padStart(2, '0');
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const year = today.getFullYear();

    return `${day}/${month}/${year}`;
  }
}
