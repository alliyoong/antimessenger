import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AccountByAdminService } from '../account-by-admin.service';
import { Account } from '../account';
import { take } from 'rxjs';
import { SnackbarNotiService } from '../../snackbar-notification/snackbar-noti.service';
import { NotificationType } from '../../enum/notification-type.enum';

@Component({
  selector: 'app-reset-password-confirm-dialog',
  templateUrl: './reset-password-confirm-dialog.component.html',
  styleUrls: ['./reset-password-confirm-dialog.component.css']
})
export class ResetPasswordConfirmDialogComponent {
  data: Account;
  showLoading: boolean = false;

  constructor(@Inject(MAT_DIALOG_DATA) data: Account, private notiService: SnackbarNotiService, private accountService: AccountByAdminService, private dialogRef: MatDialogRef<ResetPasswordConfirmDialogComponent>) {
    this.data = data;
  }

  resetPassword() {
    if (this.data.accountId) {
      this.showLoading = true;
      this.accountService.resetPassword(this.data.accountId).pipe(take(1)).subscribe({
        next: res => {
          this.notiService.sendNoti(NotificationType.SUCCESS, res.message);
          this.showLoading = false;
          this.dialogRef.close();
          console.log(res);
        },
        error: res => {
          this.notiService.sendNoti(NotificationType.ERROR, res.message);
          this.showLoading = false;
        },
        complete: () => {
          this.showLoading = false;
          this.dialogRef.close();
        }
      });
    }
  }
}
