import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MostPopularAllTimeComponent } from './most-popular-all-time.component';

describe('MostPopularAllTimeComponent', () => {
  let component: MostPopularAllTimeComponent;
  let fixture: ComponentFixture<MostPopularAllTimeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MostPopularAllTimeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MostPopularAllTimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
