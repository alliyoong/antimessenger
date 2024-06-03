import { HttpBackend, HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  config: any;

  constructor(
    private http: HttpClient,
    private httpHandler: HttpBackend
  ) {
    this.http = new HttpClient(httpHandler);
  }

  loadConfig() {
    return this.http
      .get('./assets/config.json')
      .pipe(tap(config => this.config = config));
  }

}
