import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CatalogDisciplinaModule } from './disciplina/disciplina.module';
import { CatalogNotaModule } from './nota/nota.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        CatalogDisciplinaModule,
        CatalogNotaModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CatalogEntityModule {}
