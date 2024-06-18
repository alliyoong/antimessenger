import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
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
import { CustomValidators } from '../validators/custom-validators';
import { RouterModule } from '@angular/router';
import { MessAccount } from '../domain/mess-account';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [
    RouterModule,
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
      username: new FormControl('', { validators: [Validators.required] }),
      password: new FormControl('', { validators: [Validators.required, CustomValidators.validatePassword] })
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
        const currentUser: MessAccount = res.body?.data.account;
        this.userService.cacheToken(token);
        this.userService.cacheUser(currentUser);
        this.notiService.sendNoti(NotificationType.SUCCESS, res.body!.message);
        this.showLoading = false;
        this.router.navigate(['/home']);
      },
      error: res => {
        console.log(res);
        const message = res.error.message;
        this.notiService.sendNoti(NotificationType.ERROR, message);
        this.showLoading = false;
      }
    });
  }

  ngOnDestroy(): void {
  }

}
