import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { CustomValidator } from '../../validator/custom-validator';
import { Account, Role } from '../account';
import { Observable, take } from 'rxjs';
import { AccountByAdminService } from '../account-by-admin.service';
import { SnackbarNotiService } from '../../snackbar-notification/snackbar-noti.service';
import { NotificationType } from '../../enum/notification-type.enum';

@Component({
  selector: 'app-add-account',
  templateUrl: './add-account.component.html',
  styleUrls: ['./add-account.component.css']
})
export class AddAccountComponent implements OnInit{
  addAccountForm!: FormGroup;
  showLoading: boolean = false;
  hide: boolean = true;
  roleList$!: Observable<Role[]>;

  imageFiles: any[] = [];
  toAddFile?: File|null;
  fileOverSizeError: boolean = false;
  fileExtensionError: boolean = false;

  constructor(private notiService: SnackbarNotiService, private accountService: AccountByAdminService, private formBuilder: FormBuilder){}

  ngOnInit(): void {
    this.addAccountForm = this.formBuilder.group({
      username: new FormControl('', [Validators.required]),
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required, CustomValidator.validatePassword]),
      email: new FormControl('', [Validators.required, Validators.email]),
      phone: new FormControl('', [CustomValidator.validatePhoneNumber]),
      bio: new FormControl(''),
      address: new FormControl(''),
      role: new FormControl('', [Validators.required]),
      isEnabled: new FormControl(false),
      isUsingMfa: new FormControl(false),
      isNonLocked: new FormControl(false)
    });
    
    this.roleList$ = this.accountService.getListRole();
  }
  
  addAccount(){
    this.showLoading = true;
    const fd = new FormData();
    fd.append('account', this.createAccountBlob());
    if(this.toAddFile) {
      fd.append('profileImage', this.toAddFile);
    }
    this.accountService.addAccount(fd).pipe(take(1)).subscribe({
      next: res => { 
         this.showLoading = false;
         this.notiService.sendNoti(NotificationType.SUCCESS, res.message);
         this.resetForm();
      },
      error: res => {
         this.showLoading = false;
         // this.notiService.sendNoti('green-snackbar', res.message);
         // this.resetForm();
      },
      complete: () => this.showLoading = false
    }
    );
  }
  
  onFileDropped(event: FileList): void{
    this.imageFiles.push(event[0]);
    this.toAddFile = this.prepareFile();
  }

  fileBrowseHandler(event: Event): void{
    this.imageFiles.push((event.target as HTMLInputElement).files![0]);
    this.toAddFile = this.prepareFile();
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
    const data = this.addAccountForm.getRawValue();
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
    console.log(account);
    const jsonObject = JSON.stringify(account);
    const blob = new Blob([jsonObject], {
      type: 'application/json'
    });
    return blob;
  }
  
  resetForm(): void{
    this.addAccountForm.reset({
      username:'',
      firstName: '',
      lastName: '',
      password: '',
      email: '',
      phone: '',
      bio: '',
      address: '',
      role: '',
      isEnabled: false,
      isUsingMfa: false,
      isNonLocked: false
    });
    this.addAccountForm.markAsPristine();
  }

}
