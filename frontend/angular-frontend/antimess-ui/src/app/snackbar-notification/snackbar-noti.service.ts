import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class SnackbarNotiService {

  constructor(private snackbar: MatSnackBar){}

  sendNoti(type: string, message: string){
      const snackbarConfig = new MatSnackBarConfig();
      snackbarConfig.verticalPosition = 'top';
      snackbarConfig.horizontalPosition = 'center';
      snackbarConfig.duration = 5000;
      snackbarConfig.panelClass = [type]
      
      if(!message) {
        message = 'Something happened';
      }  
        this.snackbar.open(message, `X`, snackbarConfig);
  }
}
