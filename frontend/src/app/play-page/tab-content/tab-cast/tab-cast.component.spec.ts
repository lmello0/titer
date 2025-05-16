import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TabCastComponent } from './tab-cast.component';

describe('TabCastComponent', () => {
  let component: TabCastComponent;
  let fixture: ComponentFixture<TabCastComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TabCastComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TabCastComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
