import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { INota } from 'app/shared/model/nota.model';

type EntityResponseType = HttpResponse<INota>;
type EntityArrayResponseType = HttpResponse<INota[]>;

@Injectable({ providedIn: 'root' })
export class NotaService {
    public resourceUrl = SERVER_API_URL + 'api/notas';

    constructor(protected http: HttpClient) {}

    create(nota: INota): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(nota);
        return this.http
            .post<INota>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(nota: INota): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(nota);
        return this.http
            .put<INota>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<INota>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<INota[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(nota: INota): INota {
        const copy: INota = Object.assign({}, nota, {
            data: nota.data != null && nota.data.isValid() ? nota.data.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.data = res.body.data != null ? moment(res.body.data) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((nota: INota) => {
                nota.data = nota.data != null ? moment(nota.data) : null;
            });
        }
        return res;
    }
}
