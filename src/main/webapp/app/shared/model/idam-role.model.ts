import { IIdamUser } from 'app/shared/model/idam-user.model';

export interface IIdamRole {
  id?: number;
  roleName?: string;
  members?: IIdamUser[];
}

export class IdamRole implements IIdamRole {
  constructor(public id?: number, public roleName?: string, public members?: IIdamUser[]) {}
}
