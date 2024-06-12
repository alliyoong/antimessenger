import { Component, ElementRef, ViewChild } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { SharedService } from '../services/shared.service';

@Component({
  selector: 'app-nav-header',
  templateUrl: './nav-header.component.html',
  styleUrls: ['./nav-header.component.scss']
})
export class NavHeaderComponent {
  searchText: string = '';
  @ViewChild('searchBar') searchBar!: ElementRef;
  toggleSearch: boolean = false;

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private breakpointObserver: BreakpointObserver, private sharedService: SharedService) { }

  openSearch(): void {
    this.searchBar.nativeElement.focus();
    this.toggleSearch = true;
  }

  closeSearch(): void {
    this.searchText = '';
    this.sharedService.searchValue = '';
    this.toggleSearch = false;
  }

  searchUser(): void {
    this.sharedService.searchValue = this.searchText;
  }

}
