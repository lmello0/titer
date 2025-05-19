import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PlayPosterComponent } from './play-poster/play-poster.component';
import { PlayTitleComponent } from './play-title/play-title.component';
import { TabContentComponent } from './tab-content/tab-content.component';
import { CommentSectionComponent } from './comment-section/comment-section.component';
import { MobileNavbarOpenService } from '../services/menu-open/mobile-navbar-open.service';
import { PlayDetails } from '../shared/interfaces/play-details.interface';
import { PlayService } from '../services/play/play.service';

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
  play!: PlayDetails;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    public isMenuOpenService: MobileNavbarOpenService,
    private readonly playService: PlayService,
  ) {}

  ngOnInit(): void {
    const playId = this.route.snapshot.paramMap.get('id');

    if (!playId) {
      this.router.navigate(['/']);
      return;
    }

    this.playService.getPlayById(playId).subscribe((p) => {
      if (!p) {
        this.router.navigate(['/']);
        return;
      }

      this.play = p;
    });
  }
}
