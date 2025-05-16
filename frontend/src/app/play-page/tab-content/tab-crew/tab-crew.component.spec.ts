import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TabCrewComponent } from './tab-crew.component';

describe('TabCrewComponent', () => {
  let component: TabCrewComponent;
  let fixture: ComponentFixture<TabCrewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TabCrewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TabCrewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
