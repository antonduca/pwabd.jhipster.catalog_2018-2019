import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IDisciplina } from 'app/shared/model/disciplina.model';
import { DisciplinaService } from './disciplina.service';

@Component({
    selector: 'jhi-disciplina-update',
    templateUrl: './disciplina-update.component.html'
})
export class DisciplinaUpdateComponent implements OnInit {
    disciplina: IDisciplina;
    isSaving: boolean;

    constructor(protected disciplinaService: DisciplinaService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ disciplina }) => {
            this.disciplina = disciplina;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.disciplina.id !== undefined) {
            this.subscribeToSaveResponse(this.disciplinaService.update(this.disciplina));
        } else {
            this.subscribeToSaveResponse(this.disciplinaService.create(this.disciplina));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDisciplina>>) {
        result.subscribe((res: HttpResponse<IDisciplina>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
