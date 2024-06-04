import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { NotificationType } from '../constants/notification-type.enum';
import { SnackbarNotiService } from '../services/snackbar-noti.service';
import { UserService } from '../services/user.service';
import { CustomValidators } from '../validators/custom-validators';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
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
export class RegisterComponent implements OnInit, OnDestroy {
  registerForm !: FormGroup;
  hide: boolean;
  confirmHide: boolean;
  subscriptions: Subscription[];
  showLoading: boolean; //for showing spinner

  constructor(private notiService: SnackbarNotiService,
    private formBuilder: FormBuilder,
    private userService: UserService) {
    this.hide = true;
    this.confirmHide = true;
    this.showLoading = false;
    this.subscriptions = [];
  }

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      firstName: ['', { validators: [Validators.required] }],
      lastName: ['', { validators: [Validators.required] }],
      username: ['', { validators: [Validators.required] }],
      password: ['', { validators: [Validators.required, CustomValidators.validatePassword] }],
      confirmPassword: ['', { validators: [Validators.required] }],
      email: ['', { validators: [Validators.required, Validators.email], /*updateOn: 'blur'*/ }]
    },
      { validators: CustomValidators.validateMatchPassword }
    )
  };

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
      this.userService.register(fd).subscribe({
        next: response => {
          this.showLoading = false;
          this.notiService.sendNoti(NotificationType.SUCCESS, response.message);
          this.resetForm();
        },
        error: response => {
          this.showLoading = false;
          const message = response.message || response.body!.message;
          this.notiService.sendNoti(NotificationType.ERROR, message);
          // this.notiService.sendNoti(NotificationType.ERROR, response.error.message);
        },
        complete: () => this.showLoading = false
      })
    );

  }
  resetForm(): void {
    this.registerForm.reset();
    this.registerForm.markAsPristine();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(
      subscription => subscription.unsubscribe()
    );
  }
}
