import { Injectable } from '@angular/core';
import { ConfigService } from '../utilities/config.service';
import { HttpClient } from '@angular/common/http';
import { CustomHttpResponse } from '../manage-account/custom-http-response';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VerifyAccountService {
  private baseUrl: string = this.configService.config.apiUrl;

  constructor(private configService : ConfigService, private http: HttpClient) { }
  
  verifyAccount(key: string): Observable<CustomHttpResponse>{
    return this.http.get<CustomHttpResponse>(`${this.baseUrl}/verify/account/${key}`);
  }
}
