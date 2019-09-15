import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMaterialList } from 'app/shared/model/box/material-list.model';

type EntityResponseType = HttpResponse<IMaterialList>;
type EntityArrayResponseType = HttpResponse<IMaterialList[]>;

@Injectable({ providedIn: 'root' })
export class MaterialListService {
  public resourceUrl = SERVER_API_URL + 'services/box/api/material-lists';

  constructor(protected http: HttpClient) {}

  create(materialList: IMaterialList): Observable<EntityResponseType> {
    return this.http.post<IMaterialList>(this.resourceUrl, materialList, { observe: 'response' });
  }

  update(materialList: IMaterialList): Observable<EntityResponseType> {
    return this.http.put<IMaterialList>(this.resourceUrl, materialList, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMaterialList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMaterialList[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
