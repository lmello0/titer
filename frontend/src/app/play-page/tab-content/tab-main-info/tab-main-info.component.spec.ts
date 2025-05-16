import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TabMainInfoComponent } from './tab-main-info.component';

describe('TabMainInfoComponent', () => {
  let component: TabMainInfoComponent;
  let fixture: ComponentFixture<TabMainInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TabMainInfoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TabMainInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
