import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBoxitem } from 'app/shared/model/box/boxitem.model';

type EntityResponseType = HttpResponse<IBoxitem>;
type EntityArrayResponseType = HttpResponse<IBoxitem[]>;

@Injectable({ providedIn: 'root' })
export class BoxitemService {
  public resourceUrl = SERVER_API_URL + 'services/box/api/boxitems';

  constructor(protected http: HttpClient) {}

  create(boxitem: IBoxitem): Observable<EntityResponseType> {
    return this.http.post<IBoxitem>(this.resourceUrl, boxitem, { observe: 'response' });
  }

  update(boxitem: IBoxitem): Observable<EntityResponseType> {
    return this.http.put<IBoxitem>(this.resourceUrl, boxitem, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBoxitem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBoxitem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
