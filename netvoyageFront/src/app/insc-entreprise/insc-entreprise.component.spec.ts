import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscEntrepriseComponent } from './insc-entreprise.component';

describe('InscEntrepriseComponent', () => {
  let component: InscEntrepriseComponent;
  let fixture: ComponentFixture<InscEntrepriseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscEntrepriseComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscEntrepriseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
