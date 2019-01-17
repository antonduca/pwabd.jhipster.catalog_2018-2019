/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CatalogTestModule } from '../../../test.module';
import { NotaUpdateComponent } from 'app/entities/nota/nota-update.component';
import { NotaService } from 'app/entities/nota/nota.service';
import { Nota } from 'app/shared/model/nota.model';

describe('Component Tests', () => {
    describe('Nota Management Update Component', () => {
        let comp: NotaUpdateComponent;
        let fixture: ComponentFixture<NotaUpdateComponent>;
        let service: NotaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CatalogTestModule],
                declarations: [NotaUpdateComponent]
            })
                .overrideTemplate(NotaUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(NotaUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotaService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Nota(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.nota = entity;
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
                    const entity = new Nota();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.nota = entity;
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
