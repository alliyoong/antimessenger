import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { Observable, shareReplay } from 'rxjs';
import { CustomHttpResponse } from '../domain/custom-http-response';

@Injectable({
  providedIn: 'root'
})
export class ChatMessageService {
  public livechatUrl: string = `${this.configService.config.livechatUrl}`;

  constructor(
    private configService: ConfigService,
    private http: HttpClient,
  ) { }

  getChatHistory(senderId: number, recipientId: number): Observable<CustomHttpResponse> {
    return this.http.get<CustomHttpResponse>(
      `${this.livechatUrl}/chat-message/${senderId}/${recipientId}`,
      {}
    ).pipe(
      shareReplay(1)
    );
  }
}
