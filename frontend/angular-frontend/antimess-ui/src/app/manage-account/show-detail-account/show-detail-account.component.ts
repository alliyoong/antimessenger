import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Account } from '../account';

@Component({
  selector: 'app-show-detail-account',
  templateUrl: './show-detail-account.component.html',
  styleUrls: ['./show-detail-account.component.css']
})
export class ShowDetailAccountComponent{
  account!: Account;
  constructor(@Inject(MAT_DIALOG_DATA) data: Account){
    this.account = data;
  }
}
