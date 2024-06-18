import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { SharedState } from '../domain/shared-state';

@Injectable({
  providedIn: 'root'
})
export class SharedService {
  searchSubject = new BehaviorSubject<SharedState>({
    searchValue: '',
    activeTab: 0
  });
  searchSubject$ = this.searchSubject.asObservable();

  constructor() { }
}
