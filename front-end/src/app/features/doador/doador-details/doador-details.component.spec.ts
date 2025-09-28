import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoadorDetailsComponent } from './doador-details.component';

describe('DoadorDetailsComponent', () => {
  let component: DoadorDetailsComponent;
  let fixture: ComponentFixture<DoadorDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DoadorDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoadorDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
