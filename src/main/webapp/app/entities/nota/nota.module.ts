import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CatalogSharedModule } from 'app/shared';
import { CatalogAdminModule } from 'app/admin/admin.module';
import {
    NotaComponent,
    NotaDetailComponent,
    NotaUpdateComponent,
    NotaDeletePopupComponent,
    NotaDeleteDialogComponent,
    notaRoute,
    notaPopupRoute
} from './';

const ENTITY_STATES = [...notaRoute, ...notaPopupRoute];

@NgModule({
    imports: [CatalogSharedModule, CatalogAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [NotaComponent, NotaDetailComponent, NotaUpdateComponent, NotaDeleteDialogComponent, NotaDeletePopupComponent],
    entryComponents: [NotaComponent, NotaUpdateComponent, NotaDeleteDialogComponent, NotaDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CatalogNotaModule {}
