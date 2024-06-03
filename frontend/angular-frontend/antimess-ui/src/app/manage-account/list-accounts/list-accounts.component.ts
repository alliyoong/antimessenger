import {
  ChangeDetectorRef,
  AfterViewInit,
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  ViewChild,
  ViewContainerRef
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { SnackbarNotiService } from '../../snackbar-notification/snackbar-noti.service';
import { Account } from '../account';
import { DeleteAccountConfimDialogComponent } from '../delete-account-confim-dialog/delete-account-confim-dialog.component';
import { EditAccountComponent } from '../edit-account/edit-account.component';
import { ResetPasswordConfirmDialogComponent } from '../reset-password-confirm-dialog/reset-password-confirm-dialog.component';
import { ShowDetailAccountComponent } from '../show-detail-account/show-detail-account.component';
import { Router } from '@angular/router';
import { Observable, fromEvent, tap } from 'rxjs';

@Component({ selector: 'app-list-accounts', templateUrl: './list-accounts.component.html', styleUrls: ['./list-accounts.component.css'] })
export class ListAccountsComponent implements OnInit, OnDestroy, AfterViewInit{

  @ViewChild(MatPaginator) paginator !: MatPaginator;
  @ViewChild(MatSort) sort !: MatSort;
  @ViewChild(MatTable) table !: MatTable<Account>;
  @Input() accounts: Account[] = [];
  @Output() refreshEvent = new EventEmitter();
  dataSource = new MatTableDataSource<Account>;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = [ 'accountId', 'username', 'firstName', 'lastName', 'email', 'lastLoginDateDisplay', 'role', 'isEnabled', 'isUsingMfa', 'isNonLocked', 'createdAt', 'actions' ];

  constructor(
    private cd: ChangeDetectorRef, 
    private route: Router,
    private dialog: MatDialog, 
    private viewContainerRef: ViewContainerRef
    ) { }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.sort.sort({id: 'accountId', start: 'desc', disableClear: false});
    this.dataSource.data = this.accounts;
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
    this.cd.detectChanges();
  }
  
  searchTable(event: Event){
    // const allowedKey = ['KeyA','KeyB','KeyQ','KeyW','KeyE','KeyR','KeyT','KeyY','KeyU','KeyI','KeyV','KeyB','KeyN',
    // 'KeyO','KeyP','KeyS','KeyD','KeyF','KeyG','KeyH','KeyJ','KeyK','KeyL','KeyZ','KeyX','KeyC','KeyM','Digit1','Digit2','Slash',
    // 'Digit3','Digit4','Digit5','Digit6','Digit7','Digit8','Digit9','Backquote','Minus','Equal','BracketRight','BracketLeft','Backslash','Semicolon','Quote','Comma','Period'];
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLocaleLowerCase();
  }
  
  refresh(){
    this.refreshEvent.emit();
  }

  selectDetail(account: Account): void {
    this.route.navigate([`/accounts/show/${account.accountId}`])
    this.dialog.open(ShowDetailAccountComponent, {
      enterAnimationDuration: '200ms',
      width: '80%',
      data: account
    }).afterClosed().subscribe(() => this.route.navigate(['/accounts']));
  }

  selectDelete(): void {
    this.dialog.open(DeleteAccountConfimDialogComponent, {
      enterAnimationDuration: '200ms',
      disableClose: true,
      viewContainerRef: this.viewContainerRef
    }).afterClosed().subscribe((data) => { 
      this.route.navigate([ '/accounts' ]) 
      if(data.message === 'success') {
        this.refresh();
      }
    });
  }

  selectResetPassword(account: Account): void {
    this.dialog.open(ResetPasswordConfirmDialogComponent, {
      enterAnimationDuration: '200ms',
      disableClose: true,
      data: account,
      viewContainerRef: this.viewContainerRef
    });
  }

  selectEdit(account: Account): void {
    this.dialog.open(EditAccountComponent, {
      width: '50%',
      height: '90%',
      // data: account,
      data: {
        account: account
      },
      disableClose: true,
      enterAnimationDuration: '200ms',
      viewContainerRef: this.viewContainerRef
    })
    .afterClosed().subscribe((res) => { 
      this.route.navigate(['/accounts']);
      if(res.message === 'success') {
        this.refresh();
      }
    });
  }

  ngOnDestroy(): void {
  }
}
