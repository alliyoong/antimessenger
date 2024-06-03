import { Component, OnDestroy, OnInit } from '@angular/core';
import { VerifyAccountService } from './verify-account.service';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { CustomHttpResponse } from '../manage-account/custom-http-response';

@Component({
  selector: 'app-verify-account',
  templateUrl: './verify-account.component.html',
  styleUrls: ['./verify-account.component.css']
})
export class VerifyAccountComponent implements OnInit, OnDestroy{
  subcriptions: Subscription[] = [];
  isSucceeded: boolean = false;
  message: string = '';
  
  constructor(private service: VerifyAccountService, private route: ActivatedRoute){}

  ngOnInit(): void {
    const key = this.route.snapshot.paramMap.get('key');
    if(key) {
      this.subcriptions.push(
        this.service.verifyAccount(key).subscribe({
          next: (data) => {
            this.isSucceeded = true;
            this.message = data.message
          },
          error: (errorResponse) =>{
            this.isSucceeded = false;
            this.message = errorResponse.error.message;
          } 
        }
        )
      )
    }
  }

  ngOnDestroy(): void {
    this.subcriptions.forEach(sub => sub.unsubscribe());
  }

}
