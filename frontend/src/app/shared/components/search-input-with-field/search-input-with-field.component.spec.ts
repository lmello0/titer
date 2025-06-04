import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchInputWithFieldComponent } from './search-input-with-field.component';

describe('SearchInputWithFieldComponent', () => {
  let component: SearchInputWithFieldComponent;
  let fixture: ComponentFixture<SearchInputWithFieldComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchInputWithFieldComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SearchInputWithFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
