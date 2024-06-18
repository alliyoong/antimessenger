import { HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { UserService } from '../services/user.service';

export const jwtInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next) => {
  const userService = inject(UserService);
  if (req.url.includes(`${userService.antimessUrl}/login`)) {
    return next(req);
  }

  if (req.url.includes(`${userService.antimessUrl}/register`)) {
    return next(req);
  }

  if (req.url.includes(`${userService.livechatUrl}`)) {
    return next(req);
  }

  userService.loadCurrentToken();
  const authToken = userService.getCurrentToken();
  const authReq = req.clone({
    setHeaders: {
      Authorization: `Bearer ${authToken}`
    }
  });

  return next(authReq);
};
