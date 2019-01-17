/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CatalogTestModule } from '../../../test.module';
import { DisciplinaDeleteDialogComponent } from 'app/entities/disciplina/disciplina-delete-dialog.component';
import { DisciplinaService } from 'app/entities/disciplina/disciplina.service';

describe('Component Tests', () => {
    describe('Disciplina Management Delete Component', () => {
        let comp: DisciplinaDeleteDialogComponent;
        let fixture: ComponentFixture<DisciplinaDeleteDialogComponent>;
        let service: DisciplinaService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CatalogTestModule],
                declarations: [DisciplinaDeleteDialogComponent]
            })
                .overrideTemplate(DisciplinaDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DisciplinaDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DisciplinaService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
