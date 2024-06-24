import { Component, OnDestroy, OnInit } from '@angular/core';
import { User } from '../domain/user';
import { SharedService } from '../services/shared.service';
import { UserService } from '../services/user.service';
import { MatTabsModule } from '@angular/material/tabs';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { AccountDetailComponent } from '../account-detail/account-detail.component';
import { Router } from '@angular/router';
import { Subscription, switchMap } from 'rxjs';
import { SnackbarNotiService } from '../services/snackbar-noti.service';
import { NotificationType } from '../constants/notification-type.enum';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  standalone: true,
  imports: [
    MatTabsModule,
    CommonModule,
    MatButtonModule,
    MatDialogModule,
    MatIconModule,
    MatTooltipModule
  ]
})
export class HomeComponent implements OnInit, OnDestroy {
  discoverList: User[] = [];
  waitList: User[] = [];
  pendingRequests: User[] = [];
  friendList: User[] = [];
  activeTab: number = 0;
  subscriptions: Subscription[] = [];

  constructor(
    private userService: UserService,
    private sharedService: SharedService,
    private dialog: MatDialog,
    private route: Router,
    private notiService: SnackbarNotiService
  ) { }

  ngOnInit(): void {
    const currentUser = this.userService.getCurrentUser();
    const currentUserId = !!currentUser && currentUser?.userId;
    if (currentUserId) {
      this.getFriendList(currentUserId);
      this.getWaitList(currentUserId);
      this.getPendingRequests(currentUserId);
    }

    this.subscriptions.push(this.sharedService.searchSubject$.pipe(
      switchMap((state) => {
        return this.userService.searchUser(state.searchValue!)
      })
    ).subscribe({
      next: res => {
        this.activeTab = 0;
        this.discoverList = res.data.users;
        console.log(res.data.users);
      },
      error: res => {
        this.notiService.sendNoti(NotificationType.ERROR, res.message);
      }
    }));
  }

  getFriendList(userId: number): void {
    this.subscriptions.push(this.userService.getFriendList(userId).subscribe({
      next: res => {
        console.log(res);
        this.friendList = res.data.friendList;
      },
      error: res => {
        console.log(res);
        this.notiService.sendNoti(NotificationType.ERROR, res.message);
      }
    }));
  }

  getWaitList(userId: number): void {
    this.subscriptions.push(this.userService.getWaitList(userId).subscribe({
      next: res => {
        console.log(res);
        this.waitList = res.data.waitList;
      },
      error: res => {
        console.log(res);
        this.notiService.sendNoti(NotificationType.ERROR, res.message);
      }
    }));
  }

  getPendingRequests(userId: number): void {
    this.subscriptions.push(this.userService.getPendingRequests(userId).subscribe({
      next: res => {
        console.log(res);
        this.pendingRequests = res.data.pendingRequests;
      },
      error: res => {
        console.log(res);
        this.notiService.sendNoti(NotificationType.ERROR, res.message);
      }
    }));
  }

  addFriend(friend: User): void {
    const userId = this.userService.getCurrentUser()?.userId || -1;
    const friendId = friend.userId!;
    this.subscriptions.push(this.userService.addFriend(userId, friendId).subscribe({
      next: res => {
        this.notiService.sendNoti(NotificationType.SUCCESS, res.message);
      },
      error: res => {
        this.notiService.sendNoti(NotificationType.ERROR, res.error.message);
      }
    }));
  }

  startChatting(user: User) {
    this.route.navigate(['/livechat'])
  }

  cancelRequest(friend: User): void {
    const userId = this.userService.getCurrentUser()?.userId || -1;
    const friendId = friend.userId!;
    this.subscriptions.push(this.userService.cancelRequest(userId, friendId).subscribe({
      next: res => {
        this.notiService.sendNoti(NotificationType.SUCCESS, res.message);
      },
      error: res => {
        this.notiService.sendNoti(NotificationType.ERROR, res.error.message);
      }
    }))
  }

  showUserDetail(user: User): void {
    this.dialog.open(AccountDetailComponent, {
      enterAnimationDuration: '200ms',
      width: '60%',
      data: user
    }).afterClosed().subscribe(() => this.route.navigate(['/home']));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(
      sub => sub.unsubscribe()
    );
  }
}
