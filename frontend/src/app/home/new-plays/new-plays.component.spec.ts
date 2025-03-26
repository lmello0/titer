import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewPlaysComponent } from './new-plays.component';

describe('NewPlaysComponent', () => {
  let component: NewPlaysComponent;
  let fixture: ComponentFixture<NewPlaysComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewPlaysComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewPlaysComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
