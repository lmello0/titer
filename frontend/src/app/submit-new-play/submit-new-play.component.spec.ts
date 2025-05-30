import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmitNewPlayComponent } from './submit-new-play.component';

describe('SubmitNewPlayComponent', () => {
  let component: SubmitNewPlayComponent;
  let fixture: ComponentFixture<SubmitNewPlayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubmitNewPlayComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SubmitNewPlayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
