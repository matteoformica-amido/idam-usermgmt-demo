import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIdamUser } from 'app/shared/model/idam-user.model';

type EntityResponseType = HttpResponse<IIdamUser>;
type EntityArrayResponseType = HttpResponse<IIdamUser[]>;

@Injectable({ providedIn: 'root' })
export class IdamUserService {
  public resourceUrl = SERVER_API_URL + 'api/idam-users';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/idam-users';

  constructor(protected http: HttpClient) {}

  create(idamUser: IIdamUser): Observable<EntityResponseType> {
    return this.http.post<IIdamUser>(this.resourceUrl, idamUser, { observe: 'response' });
  }

  update(idamUser: IIdamUser): Observable<EntityResponseType> {
    return this.http.put<IIdamUser>(this.resourceUrl, idamUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIdamUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIdamUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIdamUser[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
