import { TestBed } from '@angular/core/testing';

import { BeefService } from './beef.service';

describe('BeefService', () => {
  let service: BeefService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BeefService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
