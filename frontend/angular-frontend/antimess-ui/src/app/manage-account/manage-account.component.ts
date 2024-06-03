import { Component, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, Observable, Subscription, catchError, finalize, of, switchMap } from 'rxjs';
import { SnackbarNotiService } from '../snackbar-notification/snackbar-noti.service';
import { Account } from './account';
import { AccountByAdminService } from './account-by-admin.service';

@Component({
  selector: 'app-manage-account',
  templateUrl: './manage-account.component.html',
  styleUrls: ['./manage-account.component.css']
})
export class ManageAccountComponent implements OnInit, OnDestroy{
  accountsSource$ = new BehaviorSubject<Account[]>([]);
  accounts$!: Observable<Account[]>;

  constructor(private accountService: AccountByAdminService, private notiService: SnackbarNotiService){}

  ngOnInit(): void {
    this.getAccounts();
  }
  
  editAccount(event: any){
    console.log(event);
  }
  deleteAccount(event: any){
    console.log(event)
  }
  
  refreshSource(){
    this.getAccounts();
    // this.accountsSource$.next([]);
  }

  getAccounts(){
    this.accounts$ = this.accountsSource$.pipe(
      switchMap(() => this.accountService.getListAccount()),
      catchError(error => {
        // console.log(error);
        return of([]);
      })
    );
  }

  ngOnDestroy(): void {
    this.accountsSource$.complete();
  }
  
}
