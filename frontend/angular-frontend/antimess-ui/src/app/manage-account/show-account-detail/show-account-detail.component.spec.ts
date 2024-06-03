import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAccountDetailComponent } from './show-account-detail.component';

describe('ShowAccountDetailComponent', () => {
  let component: ShowAccountDetailComponent;
  let fixture: ComponentFixture<ShowAccountDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowAccountDetailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowAccountDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
