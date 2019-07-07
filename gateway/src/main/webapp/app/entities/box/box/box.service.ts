import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBox } from 'app/shared/model/box/box.model';

type EntityResponseType = HttpResponse<IBox>;
type EntityArrayResponseType = HttpResponse<IBox[]>;

@Injectable({ providedIn: 'root' })
export class BoxService {
  public resourceUrl = SERVER_API_URL + 'services/box/api/boxes';

  constructor(protected http: HttpClient) {}

  create(box: IBox): Observable<EntityResponseType> {
    return this.http.post<IBox>(this.resourceUrl, box, { observe: 'response' });
  }

  update(box: IBox): Observable<EntityResponseType> {
    return this.http.put<IBox>(this.resourceUrl, box, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBox>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBox[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
