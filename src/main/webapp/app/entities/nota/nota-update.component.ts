import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { INota } from 'app/shared/model/nota.model';
import { NotaService } from './nota.service';
import { IUser, UserService } from 'app/core';
import { IDisciplina } from 'app/shared/model/disciplina.model';
import { DisciplinaService } from 'app/entities/disciplina';

@Component({
    selector: 'jhi-nota-update',
    templateUrl: './nota-update.component.html'
})
export class NotaUpdateComponent implements OnInit {
    nota: INota;
    isSaving: boolean;

    users: IUser[];

    disciplinas: IDisciplina[];
    data: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected notaService: NotaService,
        protected userService: UserService,
        protected disciplinaService: DisciplinaService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ nota }) => {
            this.nota = nota;
            this.data = this.nota.data != null ? this.nota.data.format(DATE_TIME_FORMAT) : null;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.disciplinaService.query().subscribe(
            (res: HttpResponse<IDisciplina[]>) => {
                this.disciplinas = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.nota.data = this.data != null ? moment(this.data, DATE_TIME_FORMAT) : null;
        if (this.nota.id !== undefined) {
            this.subscribeToSaveResponse(this.notaService.update(this.nota));
        } else {
            this.subscribeToSaveResponse(this.notaService.create(this.nota));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<INota>>) {
        result.subscribe((res: HttpResponse<INota>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackDisciplinaById(index: number, item: IDisciplina) {
        return item.id;
    }
}
