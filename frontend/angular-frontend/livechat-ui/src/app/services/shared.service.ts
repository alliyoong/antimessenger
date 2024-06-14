import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SharedService {
  searchSubject = new BehaviorSubject<string>("");
  searchSubject$ = this.searchSubject.asObservable();

  constructor() { }
}
