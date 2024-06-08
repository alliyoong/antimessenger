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
  public antimessUrl: string = `${this.configService.config.antimessUrl}`;
  private friendList: User[] = [];

  constructor(
    private configService: ConfigService,
    private http: HttpClient,
    private jwtHelper: JwtHelperService
  ) { }

  getFriendList(): Observable<CustomHttpResponse> {
    return this.http.get<CustomHttpResponse>(
      this.antimessUrl,
      {}
    ).pipe(
      shareReplay(1)
    );
  }

  register(fd: FormData): Observable<CustomHttpResponse> {
    return this.http.post<CustomHttpResponse>(
      `${this.antimessUrl}/register`,
      fd
    );
  }

  delete(id: string): Observable<CustomHttpResponse> {
    return this.http.delete<CustomHttpResponse>(
      `${this.antimessUrl}/${id}`
    );

  }

  update(fd: FormData, id: string): Observable<CustomHttpResponse> {
    return this.http.post<CustomHttpResponse>(`${this.antimessUrl}/${id}`, fd);
  }

  login(fd: FormData): Observable<HttpResponse<CustomHttpResponse>> {
    return this.http.post<CustomHttpResponse>(`${this.antimessUrl}/login`, fd, {
      observe: 'response'
    })
  }
}
