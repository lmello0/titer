import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayTitleComponent } from './play-title.component';

describe('PlayTitleComponent', () => {
  let component: PlayTitleComponent;
  let fixture: ComponentFixture<PlayTitleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayTitleComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PlayTitleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
