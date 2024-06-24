import { Injectable } from "@angular/core";
import { RxStomp } from "@stomp/rx-stomp";
import * as SockJS from 'sockjs-client';
import { UserService } from "./user.service";
import { User } from "../domain/user";
import { ConfigService } from "./config.service";

@Injectable({
  providedIn: 'root'
})
export class RxStompService extends RxStomp {
  constructor() {
    super();
  }
}
