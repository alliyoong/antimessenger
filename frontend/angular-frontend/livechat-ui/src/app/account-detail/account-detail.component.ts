import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { User } from '../domain/user';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-account-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule
  ],
  templateUrl: './account-detail.component.html',
  styleUrl: './account-detail.component.scss'
})
export class AccountDetailComponent {
  user!: User;
  constructor(@Inject(MAT_DIALOG_DATA) data: User) {
    this.user = data;
  }
}
