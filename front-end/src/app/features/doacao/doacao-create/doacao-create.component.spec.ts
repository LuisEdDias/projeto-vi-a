import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoacaoCreateComponent } from './doacao-create.component';

describe('DoacaoCreateComponent', () => {
  let component: DoacaoCreateComponent;
  let fixture: ComponentFixture<DoacaoCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DoacaoCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoacaoCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
