/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CatalogTestModule } from '../../../test.module';
import { NotaDetailComponent } from 'app/entities/nota/nota-detail.component';
import { Nota } from 'app/shared/model/nota.model';

describe('Component Tests', () => {
    describe('Nota Management Detail Component', () => {
        let comp: NotaDetailComponent;
        let fixture: ComponentFixture<NotaDetailComponent>;
        const route = ({ data: of({ nota: new Nota(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CatalogTestModule],
                declarations: [NotaDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(NotaDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NotaDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.nota).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
