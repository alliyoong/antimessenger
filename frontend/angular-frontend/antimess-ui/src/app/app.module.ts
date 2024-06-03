import { APP_INITIALIZER, ErrorHandler, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ManageAccountComponent } from './manage-account/manage-account.component';
import { ListAccountsComponent } from './manage-account/list-accounts/list-accounts.component';
import { NavigationHeaderComponent } from './navigation-header/navigation-header.component';
import { LayoutModule } from '@angular/cdk/layout';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { AddAccountComponent } from './manage-account/add-account/add-account.component';
import { ConfigService } from './utilities/config.service';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { VerifyAccountComponent } from './verify-account/verify-account.component';
import { ErrorStateMatcher } from '@angular/material/core';
import { CustomErrorMatcher } from './utilities/custom-error-matcher';
import { JwtModule } from '@auth0/angular-jwt';

import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSelectModule } from '@angular/material/select';
import { EditAccountComponent } from './manage-account/edit-account/edit-account.component';
import { GlobalErrorHandler } from './custom-error-handler';
import { ResetPasswordConfirmDialogComponent } from './manage-account/reset-password-confirm-dialog/reset-password-confirm-dialog.component';
import { HttpErrorInterceptor } from './http-error-interceptor';
import { DragndropDirective } from './utilities/dragndrop.directive';
import { ProgressBarComponent } from './manage-account/progress-bar/progress-bar.component';
import { DeleteAccountConfimDialogComponent } from './manage-account/delete-account-confim-dialog/delete-account-confim-dialog.component';
import { ShowAccountDetailComponent } from './manage-account/show-account-detail/show-account-detail.component';
import { ShowDetailAccountComponent } from './manage-account/show-detail-account/show-detail-account.component';
import { BooleanTransformPipe } from './utilities/boolean-transform.pipe';
import { JwtTokenInterceptor } from './jwt-token.interceptor';
import { AuthenticationGuard } from './authentication.guard';

function configFactory(configService: ConfigService) {
  return () => configService.loadConfig();
}

export function tokenGetter() {
  return localStorage.getItem('token');
}

@NgModule({
  declarations: [
    AppComponent,
    ManageAccountComponent,
    ListAccountsComponent,
    NavigationHeaderComponent,
    PageNotFoundComponent,
    AddAccountComponent,
    LoginComponent,
    RegisterComponent,
    VerifyAccountComponent,
    EditAccountComponent,
    ResetPasswordConfirmDialogComponent,
    DragndropDirective,
    ProgressBarComponent,
    DeleteAccountConfimDialogComponent,
    ShowAccountDetailComponent,
    ShowDetailAccountComponent,
    BooleanTransformPipe,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    FormsModule,
    LayoutModule,
    // angular material
    MatSelectModule,
    MatSlideToggleModule,
    MatSidenavModule,
    MatListModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatInputModule,
    MatSnackBarModule,
    MatFormFieldModule,
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatToolbarModule,
    MatPaginatorModule,
    MatSortModule,
    MatMenuModule,
    MatTooltipModule,
    MatExpansionModule,
    HttpClientModule,
    JwtModule.forRoot({
      config: {
        tokenGetter: tokenGetter,
        // allowedDomains: ['localhost:8081']
      }
    })
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: configFactory,
      deps: [ConfigService],
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtTokenInterceptor,
      multi: true
    },
    {
      provide: ErrorStateMatcher,
      useClass: CustomErrorMatcher
    },
    AuthenticationGuard
    // {
    // provide: ErrorHandler,
    // useClass: GlobalErrorHandler
    // }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
