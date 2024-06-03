import {OnInit, Component, Inject, OnDestroy, Optional, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { SnackbarNotiService } from '../../snackbar-notification/snackbar-noti.service';
import { AccountByAdminService } from '../account-by-admin.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomValidator } from '../../validator/custom-validator';
import { Observable, Subscription } from 'rxjs';
import { Account, Role } from '../account'
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NotificationType } from '../../enum/notification-type.enum';

@Component({
  selector: 'app-edit-account',
  templateUrl: './edit-account.component.html',
  styleUrls: ['./edit-account.component.css']
})
export class EditAccountComponent implements OnDestroy, OnInit{

  showLoading: boolean = false;
  hide: boolean = true;
  editAccountForm!: FormGroup;
  roleList$!: Observable<Role[]>;
  subscriptions: Subscription[] = [];

  imageFiles: any[] = [];
  toAddFile?: File|null;
  fileOverSizeError: boolean = false;
  fileExtensionError: boolean = false;

  constructor(@Optional() @Inject(MAT_DIALOG_DATA) data: any, 
              private notiService: SnackbarNotiService, 
              private formBuilder: FormBuilder, 
              private accountService: AccountByAdminService,
              private dialogRef: MatDialogRef<EditAccountComponent>
  )
  {
    const account = data.account;
    this.editAccountForm = this.formBuilder.group({
      accountId: [{value: account.accountId, disabled: true}, [Validators.required]],
      username: [account.username, [Validators.required]],
      firstName: [account.firstName, [Validators.required]],
      lastName: [account.lastName, [Validators.required]],
      email: [account.email, [Validators.required, Validators.email]],
      phone: [account.phone, [CustomValidator.validatePhoneNumber]],
      bio: [account.bio],
      address: [account.address],
      role: [account.role.roleName, [Validators.required]],
      isEnabled: [account.isEnabled, [Validators.required]],
      isUsingMfa: [account.isUsingMfa, [Validators.required]],
      isNonLocked: [account.isNonLocked, [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.roleList$ = this.accountService.getListRole();
  }
  
  editAccount(): void{
    this.showLoading = true;
    const data = this.editAccountForm.getRawValue();
    const fd = new FormData();
    fd.append('account', this.createAccountBlob());
    // if(this.toAddFile) {
      // fd.append('profileImage', this.toAddFile);
    // }
    if(this.imageFiles[0]) {
       fd.append('profileImage', this.imageFiles[0])
    }

    this.subscriptions.push(this.accountService.updateAccount(fd, data.accountId).subscribe({
       next: response => {
           this.showLoading = false;
           this.notiService.sendNoti(NotificationType.SUCCESS,response.message);
           this.dialogRef.close({message: 'success'});
       }    
      }));
  }

  // handle upload file
  onFileDropped(event: FileList): void{
    this.imageFiles.push(event[0]);
    this.toAddFile = this.prepareFile();
    // light up the submit button
    if(this.toAddFile) {
      this.editAccountForm.markAsDirty();
    }
  }

  fileBrowseHandler(event: Event): void{
    this.imageFiles.push((event.target as HTMLInputElement).files![0]);
    this.toAddFile = this.prepareFile();
    if(this.toAddFile) {
      this.editAccountForm.markAsDirty();
    }
  }

  deleteFile() {
    if(this.imageFiles.length)
      this.imageFiles.splice(0,1);
  }
  
  prepareFile(): File|null{
    this.fileExtensionError = false;
    this.fileOverSizeError = false;
    let file: any;

    if(this.imageFiles.length > 1) {
      this.imageFiles = this.imageFiles.slice(-1);
    }else if(this.imageFiles.length === 0) {
      return null;
    }    

    file = this.imageFiles[0];
    if(!this.isValidSize(file)) {
      this.fileOverSizeError = true;
      this.deleteFile();
      return null;
    }
    if(!this.isImage(file)) {
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
    const allowedTypes = ['image/png','image/jpeg'];
    return allowedTypes.includes(mimeType);
  }

  private isValidSize(file: File): boolean {
    const fileSize = file.size;
    const maxSize = 2 * 1024 * 1024; // 2MB in bytes
    return fileSize <= maxSize;
  }

  createAccountBlob(): Blob{
    const data = this.editAccountForm.getRawValue();
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
