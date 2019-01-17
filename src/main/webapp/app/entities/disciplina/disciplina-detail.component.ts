import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDisciplina } from 'app/shared/model/disciplina.model';

@Component({
    selector: 'jhi-disciplina-detail',
    templateUrl: './disciplina-detail.component.html'
})
export class DisciplinaDetailComponent implements OnInit {
    disciplina: IDisciplina;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ disciplina }) => {
            this.disciplina = disciplina;
        });
    }

    previousState() {
        window.history.back();
    }
}
