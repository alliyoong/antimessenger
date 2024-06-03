import { Injectable,ErrorHandler } from "@angular/core";
import { SnackbarNotiService } from "./snackbar-notification/snackbar-noti.service";
import { NotificationType } from "./enum/notification-type.enum";

@Injectable()
export class GlobalErrorHandler implements ErrorHandler{
    constructor(private notiService: SnackbarNotiService){}
    handleError(error: any): void {
        // this.notiService.sendNoti(NotificationType.ERROR, error.error.message);
        // throw error;
    }
}