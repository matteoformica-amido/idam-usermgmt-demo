import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIdamRole } from 'app/shared/model/idam-role.model';

type EntityResponseType = HttpResponse<IIdamRole>;
type EntityArrayResponseType = HttpResponse<IIdamRole[]>;

@Injectable({ providedIn: 'root' })
export class IdamRoleService {
  public resourceUrl = SERVER_API_URL + 'api/idam-roles';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/idam-roles';

  constructor(protected http: HttpClient) {}

  create(idamRole: IIdamRole): Observable<EntityResponseType> {
    return this.http.post<IIdamRole>(this.resourceUrl, idamRole, { observe: 'response' });
  }

  update(idamRole: IIdamRole): Observable<EntityResponseType> {
    return this.http.put<IIdamRole>(this.resourceUrl, idamRole, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIdamRole>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIdamRole[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIdamRole[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
