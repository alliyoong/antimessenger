import { Injectable } from '@angular/core';
import { ConfigService } from '../utilities/config.service';
import { Account, Role} from './account'
import { HttpClient} from '@angular/common/http';
import { CustomHttpResponse } from './custom-http-response';
import { Observable, map, shareReplay, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountByAdminService{
  baseUrl: string = `${this.configService.config.apiUrl}/admin/account`;

  constructor(
    private configService: ConfigService, 
    private http: HttpClient) { 
  }
  
  getListAccount() : Observable<Account[]>{
    return this.http.get<CustomHttpResponse>(this.baseUrl,
    {}).pipe(
      map(response => response.data.listAccount),
      shareReplay(1)
    );
  }
  
  addAccount(fd: FormData) : Observable<CustomHttpResponse>{
    return this.http.post<CustomHttpResponse>(this.baseUrl, fd);
  }
  
  updateAccount(fd: FormData, accountId: string) : Observable<CustomHttpResponse>{
    return this.http.post<CustomHttpResponse>(`${this.baseUrl}/${accountId}`, fd);
  }
  
  deleteAccount(accountId: string) : Observable<CustomHttpResponse>{
    return this.http.delete<CustomHttpResponse>(`${this.baseUrl}/${accountId}`);
  }
  
  resetPassword(accountId: string): Observable<CustomHttpResponse>{
    return this.http.get<CustomHttpResponse>(`${this.baseUrl}/reset-password/${accountId}`);
  }
  
  getListRole() : Observable<Role[]>{
    return this.http.get<CustomHttpResponse>(`${this.configService.config.apiUrl}/role`)
    .pipe(
      map(response => response.data.listRole),
      shareReplay(1)
    );
  }
  
}
