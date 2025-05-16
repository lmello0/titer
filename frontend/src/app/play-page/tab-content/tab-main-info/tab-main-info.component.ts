import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-tab-main-info',
  imports: [RouterLink],
  templateUrl: './tab-main-info.component.html',
  styleUrl: './tab-main-info.component.css',
})
export class TabMainInfoComponent {
  @Input() premiereDate!: Date;
  @Input() premiereTheatre!: { id: number; name: string };
  @Input() duration!: number;

  @Input() directors!: { id: number; name: string }[];
  @Input() mainActors!: { id: number; name: string }[];
  @Input() genres!: { id: number; name: string }[];

  @Input() socials!: {
    social: string;
    socialIcon: string | null | undefined;
    url: string;
    name: string;
  }[];

  public getDate(date: Date): string {
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();

    return `${day}/${month}/${year}`;
  }
}
