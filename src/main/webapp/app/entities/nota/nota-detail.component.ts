import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INota } from 'app/shared/model/nota.model';

@Component({
    selector: 'jhi-nota-detail',
    templateUrl: './nota-detail.component.html'
})
export class NotaDetailComponent implements OnInit {
    nota: INota;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ nota }) => {
            this.nota = nota;
        });
    }

    previousState() {
        window.history.back();
    }
}
