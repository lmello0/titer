import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LastReviewsComponent } from './last-reviews.component';

describe('LastReviewsComponent', () => {
  let component: LastReviewsComponent;
  let fixture: ComponentFixture<LastReviewsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LastReviewsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LastReviewsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
