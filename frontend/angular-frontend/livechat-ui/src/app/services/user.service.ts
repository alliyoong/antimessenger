import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { JwtHelperService } from '@auth0/angular-jwt'
import { Observable, shareReplay } from 'rxjs';
import { CustomHttpResponse } from '../domain/custom-http-response';
import { User } from '../domain/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  public baseUrl: string = `${this.configService.config.apiUrl}`;
  private friendList: User[] = [];

  constructor(
    private configService: ConfigService,
    private http: HttpClient,
    private jwtHelper: JwtHelperService
  ) { }

  getFriendList(): Observable<CustomHttpResponse> {
    return this.http.get<CustomHttpResponse>(
      this.baseUrl,
      {}
    ).pipe(
      shareReplay(1)
    );
  }

  register(fd: FormData): Observable<CustomHttpResponse> {
    return this.http.post<CustomHttpResponse>(
      `${this.baseUrl}/register`,
      fd
    );
  }

  delete(id: string): Observable<CustomHttpResponse> {
    return this.http.delete<CustomHttpResponse>(
      `${this.baseUrl}/${id}`
    );

  }

  update(fd: FormData, id: string): Observable<CustomHttpResponse> {
    return this.http.post<CustomHttpResponse>(`${this.baseUrl}/${id}`, fd);
  }

  login(fd: FormData): Observable<HttpResponse<CustomHttpResponse>> {
    return this.http.post<CustomHttpResponse>(`${this.baseUrl}/login`, fd, {
      observe: 'response'
    })
  }
}
