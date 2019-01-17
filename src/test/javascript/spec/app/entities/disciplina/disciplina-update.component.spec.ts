/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CatalogTestModule } from '../../../test.module';
import { DisciplinaUpdateComponent } from 'app/entities/disciplina/disciplina-update.component';
import { DisciplinaService } from 'app/entities/disciplina/disciplina.service';
import { Disciplina } from 'app/shared/model/disciplina.model';

describe('Component Tests', () => {
    describe('Disciplina Management Update Component', () => {
        let comp: DisciplinaUpdateComponent;
        let fixture: ComponentFixture<DisciplinaUpdateComponent>;
        let service: DisciplinaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CatalogTestModule],
                declarations: [DisciplinaUpdateComponent]
            })
                .overrideTemplate(DisciplinaUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DisciplinaUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DisciplinaService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Disciplina(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.disciplina = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Disciplina();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.disciplina = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
