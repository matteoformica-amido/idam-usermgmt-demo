export interface IIdamUser {
  id?: number;
  uid?: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  status?: string;
  roles?: string;
}

export class IdamUser implements IIdamUser {
  constructor(
    public id?: number,
    public uid?: string,
    public email?: string,
    public firstName?: string,
    public lastName?: string,
    public status?: string,
    public roles?: string
  ) {}
}
