import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMaterialListItem } from 'app/shared/model/box/material-list-item.model';

type EntityResponseType = HttpResponse<IMaterialListItem>;
type EntityArrayResponseType = HttpResponse<IMaterialListItem[]>;

@Injectable({ providedIn: 'root' })
export class MaterialListItemService {
  public resourceUrl = SERVER_API_URL + 'services/box/api/material-list-items';

  constructor(protected http: HttpClient) {}

  create(materialListItem: IMaterialListItem): Observable<EntityResponseType> {
    return this.http.post<IMaterialListItem>(this.resourceUrl, materialListItem, { observe: 'response' });
  }

  update(materialListItem: IMaterialListItem): Observable<EntityResponseType> {
    return this.http.put<IMaterialListItem>(this.resourceUrl, materialListItem, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMaterialListItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMaterialListItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
