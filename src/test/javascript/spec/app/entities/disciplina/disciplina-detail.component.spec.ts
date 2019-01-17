/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CatalogTestModule } from '../../../test.module';
import { DisciplinaDetailComponent } from 'app/entities/disciplina/disciplina-detail.component';
import { Disciplina } from 'app/shared/model/disciplina.model';

describe('Component Tests', () => {
    describe('Disciplina Management Detail Component', () => {
        let comp: DisciplinaDetailComponent;
        let fixture: ComponentFixture<DisciplinaDetailComponent>;
        const route = ({ data: of({ disciplina: new Disciplina(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CatalogTestModule],
                declarations: [DisciplinaDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DisciplinaDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DisciplinaDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.disciplina).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
