import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { SnackbarNotiService } from '../services/snackbar-noti.service';
import { take } from 'rxjs';
import { HeaderType } from '../constants/header-type';
import { User } from '../domain/user';
import { NotificationType } from '../constants/notification-type.enum';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatIconModule,
    MatProgressSpinnerModule,
    CommonModule
  ]
})
export class LoginComponent implements OnInit, OnDestroy {
  loginForm !: FormGroup;
  username !: string;
  password !: string;
  hide: boolean = true;
  showLoading: boolean = false;

  constructor(
    private userService: UserService,
    private notiService: SnackbarNotiService,
    private router: Router,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: new FormControl('', { validators: [] }),
      password: new FormControl('', { validators: [] })
    })
  }

  login(): void {
    this.showLoading = true;
    const fd = new FormData();
    fd.append('username', this.loginForm.getRawValue().username);
    fd.append('password', this.loginForm.getRawValue().password);
    this.userService.login(fd).pipe(take(1)).subscribe({
      next: res => {
        const token = res.headers.get(HeaderType.JWT_TOKEN);
        const currentUser: User = res.body?.data;
        this.notiService.sendNoti(NotificationType.SUCCESS, res.body!.message);
        this.showLoading = false;
        this.router.navigate(['/friendlist']);
      },
      error: res => {
        this.notiService.sendNoti(NotificationType.ERROR, res.body!.message);
        this.showLoading = false;
      }
    });
  }

  ngOnDestroy(): void {
    throw new Error('Method not implemented.');
  }

}
