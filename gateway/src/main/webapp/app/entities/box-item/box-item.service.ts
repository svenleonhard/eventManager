import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBoxItem } from 'app/shared/model/box-item.model';

type EntityResponseType = HttpResponse<IBoxItem>;
type EntityArrayResponseType = HttpResponse<IBoxItem[]>;

@Injectable({ providedIn: 'root' })
export class BoxItemService {
  public resourceUrl = SERVER_API_URL + 'api/box-items';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/box-items';

  constructor(protected http: HttpClient) {}

  create(boxItem: IBoxItem): Observable<EntityResponseType> {
    return this.http.post<IBoxItem>(this.resourceUrl, boxItem, { observe: 'response' });
  }

  update(boxItem: IBoxItem): Observable<EntityResponseType> {
    return this.http.put<IBoxItem>(this.resourceUrl, boxItem, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBoxItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBoxItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBoxItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
