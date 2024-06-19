import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { JwtHelperService } from '@auth0/angular-jwt'
import { Observable, shareReplay } from 'rxjs';
import { CustomHttpResponse } from '../domain/custom-http-response';
import { User } from '../domain/user';
import { SharedService } from './shared.service';
import { MessAccount } from '../domain/mess-account';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  public antimessUrl: string = `${this.configService.config.antimessUrl}`;
  public livechatUrl: string = `${this.configService.config.livechatUrl}`;

  private loggedInUsername!: string | null;
  private token!: string | null;
  private currentUser!: User;

  constructor(
    private configService: ConfigService,
    private http: HttpClient,
    private jwtHelper: JwtHelperService
  ) { }

  searchUser(value: string): Observable<CustomHttpResponse> {
    return this.http.get<CustomHttpResponse>(
      `${this.livechatUrl}/user/search/${value}`
    );
  }

  addFriend(userId: number, friendId: number): Observable<CustomHttpResponse> {
    return this.http.get<CustomHttpResponse>(
      `${this.livechatUrl}/user/add-friend/${userId}/${friendId}`
    );
  }

  cancelRequest(userId: number, friendId: number): Observable<CustomHttpResponse> {
    return this.http.get<CustomHttpResponse>(
      `${this.livechatUrl}/user/cancel-request/${userId}/${friendId}`
    );
  }

  getFriendList(id: number): Observable<CustomHttpResponse> {
    return this.http.get<CustomHttpResponse>(
      `${this.livechatUrl}/user/friend-list/${id}`,
      {}
    ).pipe(
      shareReplay(1)
    );
  }

  getPendingRequests(id: number): Observable<CustomHttpResponse> {
    return this.http.get<CustomHttpResponse>(
      `${this.livechatUrl}/user/pending-requests/${id}`,
      {}
    ).pipe(
      shareReplay(1)
    );
  }

  getWaitList(id: number): Observable<CustomHttpResponse> {
    return this.http.get<CustomHttpResponse>(
      `${this.livechatUrl}/user/wait-list/${id}`,
      {}
    ).pipe(
      shareReplay(1)
    );
  }

  getDiscover(id: string): Observable<CustomHttpResponse> {
    return this.http.get<CustomHttpResponse>(
      `${this.livechatUrl}/discover/${id}`,
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

  logOut(): void {
    this.token = null;
    this.loggedInUsername = null;
    localStorage.removeItem('antimess-user');
    localStorage.removeItem('antimess-token');
  }

  cacheToken(token: string | null): void {
    this.token = token;
    localStorage.setItem('antimess-token', this.token!);
  }

  cacheUser(account: MessAccount): void {
    const user = this.mapMessAccountToUser(account);
    this.currentUser = user;
    localStorage.setItem('antimess-user', JSON.stringify(this.currentUser));
  }

  getCurrentUser(): User | null {
    if (localStorage.getItem('antimess-user')) {
      return JSON.parse(localStorage.getItem('antimess-user')!);
    }
    return null;
  }

  loadCurrentToken(): void {
    this.token = localStorage.getItem('antimess-token');
  }

  mapMessAccountToUser(account: MessAccount): User {
    let user: { [k: string]: any } = {};
    let key: keyof MessAccount;
    for (key in account) {
      if (key !== 'accountId') {
        user = {
          ...user,
          [key]: account[key]
        }
      }
    }
    user['userId'] = account.accountId;
    return user as User;
  }

  getCurrentToken(): string {
    return this.token!;
  }

  isLoggedIn(): boolean {
    this.loadCurrentToken();
    if (this.token) {
      if (this.jwtHelper.decodeToken(this.token).sub != null || '') {
        if (!this.jwtHelper.isTokenExpired(this.token)) {
          this.loggedInUsername = this.jwtHelper.decodeToken(this.token).sub;
          return true;
        }
      }
    } else {
      this.logOut();
      return false;
    }
    return false;
  }

  isContainUser(toCheck: User, list: User[]): boolean {
    return !!list.find(user => user.userId === toCheck.userId);
  }

}
