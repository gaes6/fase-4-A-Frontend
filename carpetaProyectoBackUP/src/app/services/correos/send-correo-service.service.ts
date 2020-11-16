import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RespuestasServices } from '../../Models/respuestasServices';

@Injectable({
  providedIn: 'root'
})
export class SendCorreoServiceService {

  constructor(private httpClient: HttpClient) { }

  public recuperar(email: string): Observable<RespuestasServices> {
    return this.httpClient.get<RespuestasServices>('/api/usuario/recuperar/email=' + email);
  }
}