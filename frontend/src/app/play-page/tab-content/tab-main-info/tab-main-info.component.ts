import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Theatre } from '../../../shared/interfaces/theatre';
import { Director } from '../../../shared/interfaces/director';
import { Actor } from '../../../shared/interfaces/actor';
import { Genre } from '../../../shared/interfaces/genre';
import { Social } from '../../../shared/interfaces/social';

@Component({
  selector: 'app-tab-main-info',
  imports: [RouterLink],
  templateUrl: './tab-main-info.component.html',
  styleUrl: './tab-main-info.component.css',
})
export class TabMainInfoComponent {
  @Input() premiereDate!: Date;
  @Input() premiereTheatre!: Theatre;
  @Input() duration!: number;

  @Input() directors!: Director[];
  @Input() mainActors!: Actor[];
  @Input() genres!: Genre[];
  @Input() socials!: Social[];

  public getDate(date: Date): string {
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();

    return `${day}/${month}/${year}`;
  }
}
