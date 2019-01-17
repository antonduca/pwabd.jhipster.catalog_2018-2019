import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDisciplina } from 'app/shared/model/disciplina.model';
import { DisciplinaService } from './disciplina.service';

@Component({
    selector: 'jhi-disciplina-delete-dialog',
    templateUrl: './disciplina-delete-dialog.component.html'
})
export class DisciplinaDeleteDialogComponent {
    disciplina: IDisciplina;

    constructor(
        protected disciplinaService: DisciplinaService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.disciplinaService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'disciplinaListModification',
                content: 'Deleted an disciplina'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-disciplina-delete-popup',
    template: ''
})
export class DisciplinaDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ disciplina }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DisciplinaDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.disciplina = disciplina;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
