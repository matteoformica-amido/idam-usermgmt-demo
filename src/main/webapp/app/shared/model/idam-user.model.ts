import { IIdamRole } from 'app/shared/model/idam-role.model';

export interface IIdamUser {
  id?: number;
  uid?: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  status?: string;
  roles?: IIdamRole[];
}

export class IdamUser implements IIdamUser {
  constructor(
    public id?: number,
    public uid?: string,
    public email?: string,
    public firstName?: string,
    public lastName?: string,
    public status?: string,
    public roles?: IIdamRole[]
  ) {}
}
