import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { SnackbarNotiService } from '../services/snackbar-noti.service';
import { UserService } from '../services/user.service';
import { CustomValidators } from '../validators/custom-validators';
import { NotificationType } from '../constants/notification-type.enum';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-my-account',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatProgressSpinnerModule,
    CommonModule
  ],
  templateUrl: './my-account.component.html',
  styleUrl: './my-account.component.scss'
})
export class MyAccountComponent implements OnInit, OnDestroy {
  showLoading: boolean = false;
  hide: boolean = true;
  editUserForm!: FormGroup;
  subscriptions: Subscription[] = [];

  imageFiles: any[] = [];
  toAddFile?: File | null;
  fileOverSizeError: boolean = false;
  fileExtensionError: boolean = false;

  constructor(
    private notiService: SnackbarNotiService,
    private formBuilder: FormBuilder,
    private userService: UserService
  ) {
    const user = this.userService.getCurrentUser();
    if (user) {
      this.editUserForm = this.formBuilder.group({
        userId: [{ value: user.userId, disabled: true }, [Validators.required]],
        username: [user.username, [Validators.required]],
        firstName: [user.firstName, [Validators.required]],
        lastName: [user.lastName, [Validators.required]],
        email: [user.email, [Validators.required, Validators.email]],
        phone: [user.phone, [CustomValidators.validatePhoneNumber]],
        bio: [user.bio],
        address: [user.address],
        // isEnabled: [user.isEnabled, [Validators.required]],
        // isUsingMfa: [user.isUsingMfa, [Validators.required]],
        // isNonLocked: [user.isNonLocked, [Validators.required]]
      });
    }
  }

  ngOnInit(): void {
  }

  editUser(): void {
    this.showLoading = true;
    const data = this.editUserForm.getRawValue();
    const fd = new FormData();
    fd.append('account', this.createUserBlob());
    if (this.imageFiles[0]) {
      fd.append('profileImage', this.imageFiles[0])
    }

    this.subscriptions.push(this.userService.update(fd, data.userId).subscribe({
      next: response => {
        this.showLoading = false;
        this.notiService.sendNoti(NotificationType.SUCCESS, response.message);
      },
      error: response => {
        this.notiService.sendNoti(NotificationType.ERROR, response.error.message);
        this.showLoading = false;
      }
    }));
  }

  // handle upload file
  onFileDropped(event: any): void {
    this.imageFiles.push(event[0]);
    this.toAddFile = this.prepareFile();
    // light up the submit button
    if (this.toAddFile) {
      this.editUserForm.markAsDirty();
    }
  }

  fileBrowseHandler(event: Event): void {
    this.imageFiles.push((event.target as HTMLInputElement).files![0]);
    this.toAddFile = this.prepareFile();
    if (this.toAddFile) {
      this.editUserForm.markAsDirty();
    }
  }

  deleteFile() {
    if (this.imageFiles.length)
      this.imageFiles.splice(0, 1);
  }

  prepareFile(): File | null {
    this.fileExtensionError = false;
    this.fileOverSizeError = false;
    let file: any;

    if (this.imageFiles.length > 1) {
      this.imageFiles = this.imageFiles.slice(-1);
    } else if (this.imageFiles.length === 0) {
      return null;
    }

    file = this.imageFiles[0];
    if (!this.isValidSize(file)) {
      this.fileOverSizeError = true;
      this.deleteFile();
      return null;
    }
    if (!this.isImage(file)) {
      this.fileExtensionError = true;
      this.deleteFile();
      return null;
    }
    this.uploadFilesSimulator(file);
    return file;
  }

  uploadFilesSimulator(file: any) {
    file.progress = 0;
    const progressInterval = setInterval(() => {
      if (file.progress === 100) {
        clearInterval(progressInterval);
      } else {
        file.progress += 5;
      }
    }, 200);
  }

  formatBytes(bytes: any) {
    if (bytes === 0) {
      return '0 Bytes';
    }
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }

  private isImage(file: File): boolean {
    const mimeType = file.type;
    const allowedTypes = ['image/png', 'image/jpeg'];
    return allowedTypes.includes(mimeType);
  }

  private isValidSize(file: File): boolean {
    const fileSize = file.size;
    const maxSize = 2 * 1024 * 1024; // 2MB in bytes
    return fileSize <= maxSize;
  }

  createUserBlob(): Blob {
    const data = this.editUserForm.getRawValue();
    const account = {
      username: data.username,
      firstName: data.firstName,
      lastName: data.lastName,
      password: data.password,
      email: data.email,
      phone: data.phone,
      bio: data.bio,
      address: data.address,
      role: data.role,
      isEnabled: Number(data.isEnabled),
      isUsingMfa: Number(data.isUsingMfa),
      isNonLocked: Number(data.isNonLocked),
    }
    const jsonObject = JSON.stringify(account);
    const blob = new Blob([jsonObject], {
      type: 'application/json'
    });
    return blob;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
