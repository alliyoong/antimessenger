import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Account } from '../manage-account/account';
import { AccountByUserService } from '../manage-account/account-by-user.service';
import { SnackbarNotiService } from '../snackbar-notification/snackbar-noti.service';
import { CustomValidator } from '../validator/custom-validator';
import { NotificationType } from '../enum/notification-type.enum';

@Component({selector: 'app-register', templateUrl: './register.component.html', styleUrls: ['./register.component.css']})
export class RegisterComponent implements OnInit, OnDestroy {
    registerForm !: FormGroup;
    hide : boolean;
    confirmHide: boolean;
    subscriptions : Subscription[];
    showLoading: boolean; //for showing spinner

    constructor(private notiService: SnackbarNotiService, 
        private formBuilder : FormBuilder, 
        private accountService : AccountByUserService) {
            this.hide = true;
            this.confirmHide = true;
            this.showLoading = false;
            this.subscriptions = [];
        }

    ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      firstName: ['', {validators: [Validators.required]}],
      lastName: ['', {validators: [Validators.required]}],
      username: ['', {validators: [Validators.required]}],
      password: ['', {validators: [Validators.required, CustomValidator.validatePassword]}],
      confirmPassword: ['', {validators: [Validators.required]}],
      email: ['', {validators: [Validators.required, Validators.email], /*updateOn: 'blur'*/}]
        },
        {validators: CustomValidator.validateMatchPassword}
    )};

    register() {
        const data = this.registerForm.getRawValue();
        const fd = new FormData();
        fd.append('firstName', data.firstName);
        fd.append('lastName', data.lastName);
        fd.append('email', data.email);
        fd.append('username', data.username);
        fd.append('password', data.password);

        // if(this.registerForm.invalid) {
            // this.registerForm.markAllAsTouched();
        // } else {
            // this.registerForm.reset();
        // }
        // show spinner
        this.showLoading = true;

        this.subscriptions.push(
            this.accountService.addAccount(fd).subscribe({
             next: response => {
                 this.showLoading = false;
                 this.notiService.sendNoti(NotificationType.SUCCESS,response.message);
                 this.resetForm();
             },
             error: response => {
                 this.showLoading = false;
                 this.notiService.sendNoti(NotificationType.ERROR,response.error.message);
             },
             complete: () => this.showLoading = false
            })
        );
    
}
    resetForm():void{
        this.registerForm.reset();
        this.registerForm.markAsPristine();
    }

    ngOnDestroy(): void {
        this.subscriptions.forEach(
            subscription => subscription.unsubscribe()
        );
}

}
