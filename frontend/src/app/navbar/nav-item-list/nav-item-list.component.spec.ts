import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavItemListComponent } from './nav-item-list.component';

describe('NavItemListComponent', () => {
  let component: NavItemListComponent;
  let fixture: ComponentFixture<NavItemListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavItemListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavItemListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
