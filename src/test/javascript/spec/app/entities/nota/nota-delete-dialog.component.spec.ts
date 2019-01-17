/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CatalogTestModule } from '../../../test.module';
import { NotaDeleteDialogComponent } from 'app/entities/nota/nota-delete-dialog.component';
import { NotaService } from 'app/entities/nota/nota.service';

describe('Component Tests', () => {
    describe('Nota Management Delete Component', () => {
        let comp: NotaDeleteDialogComponent;
        let fixture: ComponentFixture<NotaDeleteDialogComponent>;
        let service: NotaService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CatalogTestModule],
                declarations: [NotaDeleteDialogComponent]
            })
                .overrideTemplate(NotaDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NotaDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotaService);
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
