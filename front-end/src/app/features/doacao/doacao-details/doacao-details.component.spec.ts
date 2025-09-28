import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoacaoDetailsComponent } from './doacao-details.component';

describe('DoacaoDetailsComponent', () => {
  let component: DoacaoDetailsComponent;
  let fixture: ComponentFixture<DoacaoDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DoacaoDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoacaoDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
