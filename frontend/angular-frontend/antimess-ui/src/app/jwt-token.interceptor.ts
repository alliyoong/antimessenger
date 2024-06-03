import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpHeaders
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountByUserService } from './manage-account/account-by-user.service';

@Injectable()
export class JwtTokenInterceptor implements HttpInterceptor {

  constructor(private userService: AccountByUserService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if(request.url.includes(`${this.userService.baseUrl}/login`)) {
      return next.handle(request);
    }
    if(request.url.includes(`${this.userService.baseUrl}/register`)) {
      return next.handle(request);
    }
    this.userService.loadCurrentToken();
    const token = this.userService.getCurrentToken();
    const newRequest = request.clone({
      headers: new HttpHeaders({Authorization: `Bearer ${token}`})
    });
    return next.handle(newRequest);
  }
}
