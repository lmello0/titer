import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-spotlight',
  imports: [],
  templateUrl: './spotlight.component.html',
  styleUrl: './spotlight.component.css',
})
export class SpotlightComponent implements OnInit {
  urls: string[] = [
    'https://film-grab.com/wp-content/uploads/2024/07/Robin-Hood-73-19.jpg',
    'https://film-grab.com/wp-content/uploads/2024/07/The-Lion-King-64.jpg',
    'https://film-grab.com/wp-content/uploads/2024/07/ATGIB-38.jpg',
    'https://film-grab.com/wp-content/uploads/2024/05/A-Matter-of-Life-and-Death-62.jpg',
    'https://film-grab.com/wp-content/uploads/2012/11/29.jpg',
  ];

  imageUrl: string = '';

  ngOnInit() {
    this.imageUrl = this.urls[Math.floor(Math.random() * this.urls.length)];
  }
}
