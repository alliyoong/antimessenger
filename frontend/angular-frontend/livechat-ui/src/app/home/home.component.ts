import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { User } from '../domain/user';
import { SharedService } from '../services/shared.service';
import { UserService } from '../services/user.service';
import { MatTabGroup, MatTabsModule } from '@angular/material/tabs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  standalone: true,
  imports: [
    MatTabsModule,
    CommonModule
  ]
})
export class HomeComponent implements OnInit, OnDestroy {
  discoverList: User[] = [
    {
      userId: '12',
      username: 'tuongnhu',
      firstName: 'tuong nhu',
      lastName: 'bui',
      email: 'tuongnhu123@emial',
    },
    {
      userId: '13',
      username: 'tuongnhu',
      firstName: 'tuong nhu',
      lastName: 'bui',
      email: 'tuongnhu123@emial',
    },
    {
      userId: '14',
      username: 'tuongnhu',
      firstName: 'tuong nhu',
      lastName: 'bui',
      email: 'tuongnhu123@emial',
    },
    {
      userId: '15',
      username: 'tuongnhu',
      firstName: 'tuong nhu',
      lastName: 'bui',
      email: 'tuongnhu123@emial',
    },
    {
      userId: '16',
      username: 'tuongnhu',
      firstName: 'tuong nhu',
      lastName: 'bui',
      email: 'tuongnhu123@emial',
    },
  ];
  waitList: User[] = [];
  pendingRequests: User[] = [];
  friendList: User[] = [];

  constructor(private userService: UserService, private sharedService: SharedService) {
  }

  ngOnInit(): void {
    const currentUser = this.userService.getCurrentUser();
    if (currentUser) {
      this.userService.getFriendList(String(currentUser.userId));
    }

    this.sharedService.searchSubject$.subscribe(searchValue => {
      this.userService.searchUser(searchValue).subscribe({
        next: res => {
          console.log(res);
        },
        error: res => {
          console.log(res);
        }
      })
    })
  }

  ngOnDestroy(): void {
    throw new Error('Method not implemented.');
  }
}
