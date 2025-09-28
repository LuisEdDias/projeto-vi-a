import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoacaoListComponent } from './doacao-list.component';

describe('DoacaoListComponent', () => {
  let component: DoacaoListComponent;
  let fixture: ComponentFixture<DoacaoListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DoacaoListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoacaoListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
