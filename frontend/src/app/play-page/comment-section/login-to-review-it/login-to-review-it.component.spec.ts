import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginToReviewItComponent } from './login-to-review-it.component';

describe('LoginToReviewItComponent', () => {
  let component: LoginToReviewItComponent;
  let fixture: ComponentFixture<LoginToReviewItComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginToReviewItComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginToReviewItComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
