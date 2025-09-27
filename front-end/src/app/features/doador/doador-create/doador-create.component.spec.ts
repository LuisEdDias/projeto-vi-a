import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoadorCreateComponent } from './doador-create.component';

describe('DoadorCreateComponent', () => {
  let component: DoadorCreateComponent;
  let fixture: ComponentFixture<DoadorCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DoadorCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoadorCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
