import { Component, OnDestroy, OnInit } from '@angular/core';
import { User } from '../domain/user';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-home-friendlist',
  templateUrl: './home-friendlist.component.html',
  styleUrls: ['./home-friendlist.component.scss']
})
export class HomeFriendlistComponent implements OnInit, OnDestroy {
  users: User[] = [];

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    const currentUser = this.userService.getCurrentUser();
    if (currentUser) {
      this.userService.getFriendList(String(currentUser.userId));
    }
  }

  ngOnDestroy(): void {
    throw new Error('Method not implemented.');
  }
}
