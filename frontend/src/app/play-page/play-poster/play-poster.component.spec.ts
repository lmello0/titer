import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayPosterComponent } from './play-poster.component';

describe('PlayPosterComponent', () => {
  let component: PlayPosterComponent;
  let fixture: ComponentFixture<PlayPosterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayPosterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlayPosterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
