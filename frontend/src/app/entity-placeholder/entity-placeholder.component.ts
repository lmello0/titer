import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-entity-placeholder',
  imports: [CommonModule, RouterLink],
  templateUrl: './entity-placeholder.component.html',
  styleUrl: './entity-placeholder.component.css',
})
export class EntityPlaceholderComponent implements OnInit {
  title = 'Page';
  subtitle = 'This view is staged for future PRD feature implementation.';

  private currentSearchQuery = '';

  constructor(private readonly route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParamMap.subscribe((queryParams) => {
      this.currentSearchQuery = queryParams.get('q') ?? '';
      this.refreshView();
    });

    this.route.paramMap.subscribe(() => {
      this.refreshView();
    });
  }

  private refreshView(): void {
    const params = this.route.snapshot.paramMap;
    const routePath = this.route.snapshot.routeConfig?.path ?? '';
    const section = routePath.split('/')[0];

    switch (section) {
      case 'list': {
        const type = params.get('type') ?? 'plays';
        this.title = this.toReadable(type);
        this.subtitle = `List page for ${this.toReadable(type)}.`;

        if (type === 'search-results' && this.currentSearchQuery) {
          this.subtitle = `Search results for "${this.currentSearchQuery}".`;
        }
        break;
      }
      case 'director': {
        const id = params.get('id') ?? 'unknown';
        this.title = `Director Profile #${id}`;
        this.subtitle = 'Detailed director information and related plays will appear here.';
        break;
      }
      case 'actor': {
        const id = params.get('id') ?? 'unknown';
        this.title = `Actor Profile #${id}`;
        this.subtitle = 'Detailed actor information and credits will appear here.';
        break;
      }
      case 'genre': {
        const id = params.get('id') ?? 'unknown';
        this.title = `Genre Page #${id}`;
        this.subtitle = 'Genre-specific filtering and discovery will appear here.';
        break;
      }
      case 'theatre': {
        const id = params.get('id') ?? 'unknown';
        this.title = `Theatre Profile #${id}`;
        this.subtitle = 'Venue profile and production history will appear here.';
        break;
      }
      case 'worker': {
        const id = params.get('id') ?? 'unknown';
        this.title = `Crew Member #${id}`;
        this.subtitle = 'Crew details and production contributions will appear here.';
        break;
      }
      case 'plays': {
        const year = params.get('year') ?? 'unknown';
        this.title = `Plays from ${year}`;
        this.subtitle = `Year-based play browse view for ${year}.`;
        break;
      }
      default:
        this.title = 'Page';
        this.subtitle = 'This view is staged for future PRD feature implementation.';
        break;
    }
  }

  private toReadable(value: string): string {
    return value
      .replace(/[-_]/g, ' ')
      .trim()
      .replace(/\b\w/g, (char) => char.toUpperCase());
  }
}
