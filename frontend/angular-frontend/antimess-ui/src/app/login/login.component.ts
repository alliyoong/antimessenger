import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription, take } from 'rxjs';
import { HeaderType } from '../enum/header-type.enum';
import { NotificationType } from '../enum/notification-type.enum';
import { AccountByUserService } from '../manage-account/account-by-user.service';
import { SnackbarNotiService } from '../snackbar-notification/snackbar-noti.service';
import { CustomValidator } from '../validator/custom-validator';
import { Account } from '../manage-account/account';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit, OnDestroy{
  loginForm !: FormGroup;
  username !: string;
  password !: string;
  subscription!: Subscription;
  hide: boolean = true;
  showLoading: boolean = false;

  constructor(private accountService: AccountByUserService, private notiService: SnackbarNotiService, private router: Router, private formBuilder: FormBuilder){}
  ngOnDestroy(): void {
    // this.subscription.unsubscribe();
  }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: new FormControl('', {validators: [Validators.required]}),
      password: new FormControl('', {validators: [Validators.required, CustomValidator.validatePassword]})
    });
  }

   getErrorMessage() : string{
    return '';
   }

   login() : void {
     this.showLoading = true;
     const loginData = this.loginForm.getRawValue();
     const fd = new FormData();
     fd.append("username",loginData.username);
     fd.append("password",loginData.password);

     this.subscription = this.accountService.login(fd).pipe(take(1)).subscribe({
      next: res => {
        const token = res.headers.get(HeaderType.JWT_TOKEN);
        const currentUser : Account = res.body?.data;
        this.accountService.cacheToken(token);
        this.accountService.cacheUser(currentUser);
        this.notiService.sendNoti(NotificationType.SUCCESS, res.body!.message);
        this.showLoading = false;
        this.router.navigate(['/accounts']);
      },
      error: res => {
        this.showLoading = false;
      }
     });
   }
   
}
