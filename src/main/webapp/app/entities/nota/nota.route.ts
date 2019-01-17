import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Nota } from 'app/shared/model/nota.model';
import { NotaService } from './nota.service';
import { NotaComponent } from './nota.component';
import { NotaDetailComponent } from './nota-detail.component';
import { NotaUpdateComponent } from './nota-update.component';
import { NotaDeletePopupComponent } from './nota-delete-dialog.component';
import { INota } from 'app/shared/model/nota.model';

@Injectable({ providedIn: 'root' })
export class NotaResolve implements Resolve<INota> {
    constructor(private service: NotaService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Nota> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Nota>) => response.ok),
                map((nota: HttpResponse<Nota>) => nota.body)
            );
        }
        return of(new Nota());
    }
}

export const notaRoute: Routes = [
    {
        path: 'nota',
        component: NotaComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'catalogApp.nota.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'nota/:id/view',
        component: NotaDetailComponent,
        resolve: {
            nota: NotaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'catalogApp.nota.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'nota/new',
        component: NotaUpdateComponent,
        resolve: {
            nota: NotaResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'catalogApp.nota.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'nota/:id/edit',
        component: NotaUpdateComponent,
        resolve: {
            nota: NotaResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'catalogApp.nota.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const notaPopupRoute: Routes = [
    {
        path: 'nota/:id/delete',
        component: NotaDeletePopupComponent,
        resolve: {
            nota: NotaResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'catalogApp.nota.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
