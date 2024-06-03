import { Injectable } from '@angular/core';
import { ConfigService } from '../utilities/config.service';
import { Account} from './account'
import { HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import { CustomHttpResponse } from './custom-http-response';
import { Observable, shareReplay, tap } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AccountByUserService {
  public baseUrl: string = `${this.configService.config.apiUrl}`;
  private accountList: Account[] = [];
  private token!: string | null;
  private loggedInUsername!: string | null;
  private currentUser!: Account;

  constructor(
    private configService: ConfigService, 
    private http: HttpClient,
    private jwtHelper: JwtHelperService
    ) { 
  }
  
  getListAccount() : Observable<CustomHttpResponse>{
    return this.http.get<CustomHttpResponse>(this.baseUrl,
    {}).pipe(
      shareReplay(1)
    );
  }
  
  addAccount(fd: FormData) : Observable<CustomHttpResponse>{
    return this.http.post<CustomHttpResponse>(`${this.baseUrl}/register`, fd);
  }
  
  updateAccount(fd: FormData, accountId: string) : Observable<CustomHttpResponse>{
    return this.http.post<CustomHttpResponse>(`${this.baseUrl}/${accountId}`, fd);
  }
  
  deleteAccount(accountId: string) : Observable<CustomHttpResponse>{
    return this.http.delete<CustomHttpResponse>(`${this.baseUrl}/${accountId}`)
  }

  login(fd: FormData): Observable<HttpResponse<CustomHttpResponse>>{
    return this.http.post<CustomHttpResponse>(`${this.baseUrl}/login`, fd, {
      // headers: new HttpHeaders({'Content-Type':`multipart/form-data; boundary=`}),
      // headers: new HttpHeaders({'Accept':`*/*`,'Access-Control-Expose-Headers': 'Content-Length,Content-Range','Access-Control-Allow-Headers': 'Accept,Accept-Language,Content-Language,Content-Type','Access-Control-Allow-Origin':'*','Access-Control-Allow-Methods': 'GET, POST, OPTIONS, PUT, DELETE'}),
      observe: 'response'
    });
  }
  
  logOut(): void{
    this.token = null;
    this.loggedInUsername = null;
    localStorage.removeItem('antimess-user');
    localStorage.removeItem('antimess-token');
  }
  
  cacheToken(token: string|null): void{
    this.token = token;
    localStorage.setItem('antimess-token', this.token!);
  }
  
  cacheUser(account: Account): void{
    this.currentUser = account;
    localStorage.setItem('antimess-user',JSON.stringify(this.currentUser));
  }
  
  getCurrentUser(): Account | null{
    if(localStorage.getItem('antimess-user')) {
      return JSON.parse(localStorage.getItem('antimess-user')!);
    }
    return null;
  }
  
  loadCurrentToken(): void {
    this.token = localStorage.getItem('antimess-token');
  }
  
  getCurrentToken(): string {
    return this.token!;
  }

  isLoggedIn(): boolean {
    this.loadCurrentToken();
    if(this.token) {
      if(this.jwtHelper.decodeToken(this.token).sub != null || '') {
        if(!this.jwtHelper.isTokenExpired(this.token)) {
          this.loggedInUsername = this.jwtHelper.decodeToken(this.token).sub;
          return true;
        }
      }
    }else{
      this.logOut();
      return false;
    }
    return false;
  }
}
