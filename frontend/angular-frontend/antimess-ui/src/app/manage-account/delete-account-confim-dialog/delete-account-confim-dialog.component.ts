import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { mergeMap } from 'rxjs';
import { AccountByAdminService } from '../account-by-admin.service';
import { SnackbarNotiService } from '../../snackbar-notification/snackbar-noti.service';
import { MatDialogRef } from '@angular/material/dialog';
import { NotificationType } from '../../enum/notification-type.enum';

@Component({
  selector: 'app-delete-account-confim-dialog',
  templateUrl: './delete-account-confim-dialog.component.html',
  styleUrls: ['./delete-account-confim-dialog.component.css']
})
export class DeleteAccountConfimDialogComponent implements OnInit{
  showLoading: boolean = false;
  accountId!: string;

  constructor(private activatedRoute: ActivatedRoute, private dialogRef: MatDialogRef<DeleteAccountConfimDialogComponent>, private notiService: SnackbarNotiService, private accountService: AccountByAdminService){}

  ngOnInit(): void {
    let param = this.activatedRoute.firstChild?.snapshot.paramMap.get('accountId');
    if (param) {
      this.accountId = param;
    }
  }

  deleteAccount(){
    this.accountService.deleteAccount(this.accountId).subscribe({
       next: response => {
           this.showLoading = false;
           this.notiService.sendNoti(NotificationType.SUCCESS,response.message);
           this.dialogRef.close({message: 'success'});
       },
       error: response => {
           this.showLoading = false;
           this.notiService.sendNoti(NotificationType.ERROR,response.error.message);
       },
       complete: () => { 
        this.showLoading = false ;
        // this.dialogRef.close();
      }
    });
  }
  
}
