import { TestBed } from '@angular/core/testing';

import { MobileNavbarOpenService } from './mobile-navbar-open.service';

describe('MenuOpenService', () => {
  let service: MobileNavbarOpenService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MobileNavbarOpenService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
