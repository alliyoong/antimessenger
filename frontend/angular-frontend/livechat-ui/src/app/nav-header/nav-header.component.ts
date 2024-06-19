import { Component, ElementRef, ViewChild } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { SharedService } from '../services/shared.service';
import { SharedState } from '../domain/shared-state';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCommonModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { RouterModule } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-nav-header',
  templateUrl: './nav-header.component.html',
  styleUrls: ['./nav-header.component.scss'],
  standalone: true,
  imports: [
    MatSidenavModule,
    MatIconModule,
    MatToolbarModule,
    MatCommonModule,
    MatButtonModule,
    FormsModule,
    MatMenuModule,
    MatDividerModule,
    RouterModule,
    MatTooltipModule
  ]
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
    this.toggleSearch = true;
    setTimeout(() => {
      this.searchBar.nativeElement.focus();
    });
  }

  closeSearch(): void {
    this.searchText = '';
    this.toggleSearch = false;
  }

  searchUser(): void {
    const sharedState: SharedState = {};
    sharedState.searchValue = this.searchText;
    this.sharedService.searchSubject.next(sharedState);
  }

}
