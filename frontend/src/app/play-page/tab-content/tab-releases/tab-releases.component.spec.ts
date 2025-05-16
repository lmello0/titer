import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TabReleasesComponent } from './tab-releases.component';

describe('TabReleasesComponent', () => {
  let component: TabReleasesComponent;
  let fixture: ComponentFixture<TabReleasesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TabReleasesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TabReleasesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
