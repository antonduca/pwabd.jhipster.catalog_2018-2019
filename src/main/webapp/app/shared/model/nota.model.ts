import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IDisciplina } from 'app/shared/model//disciplina.model';

export interface INota {
    id?: number;
    numarPuncte?: number;
    nota?: number;
    data?: Moment;
    user?: IUser;
    disciplina?: IDisciplina;
}

export class Nota implements INota {
    constructor(
        public id?: number,
        public numarPuncte?: number,
        public nota?: number,
        public data?: Moment,
        public user?: IUser,
        public disciplina?: IDisciplina
    ) {}
}
