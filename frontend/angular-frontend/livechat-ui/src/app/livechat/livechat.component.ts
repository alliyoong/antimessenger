import { AfterViewChecked, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { RxStompService } from '../services/rx-stomp.service';
import { Message, Client } from '@stomp/stompjs';
import { CommonModule } from '@angular/common';
import * as SockJs from 'sockjs-client';
import { ChatMessage } from '../domain/chat-message';
import { Subscription } from 'rxjs';
import { UserService } from '../services/user.service';
import { User } from '../domain/user';
import { SnackbarNotiService } from '../services/snackbar-noti.service';
import { NotificationType } from '../constants/notification-type.enum';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { ChatMessageService } from '../services/chat-message.service';

@Component({
  selector: 'app-livechat',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    FormsModule
  ],
  templateUrl: './livechat.component.html',
  styleUrl: './livechat.component.scss'
})
export class LivechatComponent implements OnInit, OnDestroy, AfterViewChecked {
  friendList: User[] = [];
  chatList: ChatMessage[] = [];
  client!: Client;
  currentUser!: User | null;
  currentFriend: User;
  @ViewChild('chatBox') chatBox!: ElementRef;
  @ViewChild('chatBody') chatBody!: ElementRef;
  @ViewChild('chatInput') chatInput!: ElementRef;

  // @ts-ignore
  private subscriptions: Subscription[] = [];

  constructor(
    private rxStompService: RxStompService,
    private userService: UserService,
    private notiService: SnackbarNotiService,
    private chatMessageService: ChatMessageService
  ) {
    this.currentFriend = {
      userId: -1,
      username: '',
      firstName: '',
      lastName: '',
      email: '',
    }
  }

  ngAfterViewChecked(): void {
    this.scrollDown();
  }
  scrollDown() {
    var container = this.chatBody.nativeElement;
    container.scrollTop = container.scrollHeight;
  }

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
    const userId = this.currentUser!.userId;

    if (userId) {
      this.subscriptions.push(this.privateQueue(userId!));
      this.subscriptions.push(this.publicQueue());
      this.subscriptions.push(this.getFriendList(userId!));
    }
  }

  getFriendList(userId: number): Subscription {
    return this.userService.getFriendList(userId).subscribe({
      next: res => {
        console.log(res);
        this.friendList = res.data.friendList;
        this.notiService.sendNoti(NotificationType.SUCCESS, res.message);
      },
      error: res => {
        console.log(res);
        this.notiService.sendNoti(NotificationType.ERROR, res.message);
      }
    });
  }

  showChat(friend: User): void {
    this.currentFriend = friend;
    const chatBoxEl = this.chatBox.nativeElement;
    if (!chatBoxEl.classList.contains('open')) {
      chatBoxEl.classList.add('open');
    }

    if (this.currentUser && this.currentUser.userId && this.currentFriend.userId) {
      this.subscriptions.push(this.chatMessageService.getChatHistory(this.currentUser.userId, this.currentFriend.userId).subscribe({
        next: res => {
          this.chatList = res.data.chatHistory;
          this.notiService.sendNoti(NotificationType.SUCCESS, res.message);
        },
        error: res => {
          this.notiService.sendNoti(NotificationType.ERROR, res.message);
        }
      }));
    }

  }

  closeChat(): void {
    this.chatBox.nativeElement.classList.remove('open');
  }


  privateQueue(userId: number): Subscription {
    return this.rxStompService.watch(`/user/${userId}/queue/messages`).subscribe((message: Message) => {
      const data = this.convertToChatMessage(message);
      this.chatList.push(data);
      console.log(message);
    });
  }

  publicQueue(): Subscription {
    return this.rxStompService.watch(`/user/public`).subscribe((message: Message) => {
      const data = this.convertToChatMessage(message);
      this.chatList.push(data);
      console.log(message);
    })
  }

  sendMessage() {
    const message = this.chatInput.nativeElement.value;
    const friendId = this.currentFriend.userId;
    const senderId = this.currentUser?.userId;
    const chatMessage: ChatMessage = {
      chatRoomId: '',
      senderId: senderId!,
      recipientId: friendId!,
      content: message,
      timestamp: new Date()
    }
    // this.rxStompService.publish({ destination: `/user/${friendId}/queue/messages`, body: message });
    this.rxStompService.publish({ destination: `/app/chat`, body: JSON.stringify(chatMessage) });
    this.chatList.push(chatMessage);
    this.clearInput();
  }

  convertToChatMessage(message: Message): ChatMessage {
    const chatMessage: ChatMessage = JSON.parse(message.body);
    return chatMessage;
  }

  handleInputKeyUp(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      this.sendMessage();
    }
    event.preventDefault();
  }

  clearInput() {
    this.chatInput.nativeElement.value = '';
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.rxStompService.deactivate();
  }

}
