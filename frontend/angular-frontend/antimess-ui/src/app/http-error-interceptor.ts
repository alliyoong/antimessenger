import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { Observable, catchError, delay, of, retry, throwError } from "rxjs";
import { SnackbarNotiService } from "./snackbar-notification/snackbar-noti.service";
import { NotificationType } from "./enum/notification-type.enum";

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor{
    #maxRetries = 2;
    #delayMs = 4000;
    constructor(private notiService: SnackbarNotiService){}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
        retry({
            delay: (error,count) => {
                if (error.status == 500 && count < this.#maxRetries) {
                    return of(error).pipe(delay(this.#delayMs));
                }
                throw error;
            }
        }) ,
        catchError( error => {
            let errorMessage = '';
            console.log(error);
            if(error.error instanceof ErrorEvent) {
                errorMessage = `An error occurred: ${error.error.message}`;
            } else {
                errorMessage = `An server error: ${error.status} ${error.error.message}`;
            }
            this.notiService.sendNoti(NotificationType.ERROR, errorMessage);
            return throwError(() => error);
        })
    )
  }
}
