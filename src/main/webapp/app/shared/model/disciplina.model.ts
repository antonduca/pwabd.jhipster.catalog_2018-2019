export interface IDisciplina {
    id?: number;
    denumire?: string;
    descriere?: string;
    puncteCredit?: number;
    anDeStudiu?: number;
}

export class Disciplina implements IDisciplina {
    constructor(
        public id?: number,
        public denumire?: string,
        public descriere?: string,
        public puncteCredit?: number,
        public anDeStudiu?: number
    ) {}
}
