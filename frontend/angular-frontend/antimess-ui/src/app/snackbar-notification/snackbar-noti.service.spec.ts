import { TestBed } from '@angular/core/testing';

import { SnackbarNotiService } from './snackbar-noti.service';

describe('SnackbarNotiService', () => {
  let service: SnackbarNotiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SnackbarNotiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
