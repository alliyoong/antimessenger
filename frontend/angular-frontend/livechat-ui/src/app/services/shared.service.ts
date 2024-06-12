import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SharedService {
  searchValue: string = '';

  constructor() { }
}
