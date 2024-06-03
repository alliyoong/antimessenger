import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { AddAccountComponent } from './manage-account/add-account/add-account.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { VerifyAccountComponent } from './verify-account/verify-account.component';
import { ManageAccountComponent } from './manage-account/manage-account.component';
import { EditAccountComponent } from './manage-account/edit-account/edit-account.component';
import { ShowAccountDetailComponent } from './manage-account/show-account-detail/show-account-detail.component';
import { DeleteAccountConfimDialogComponent } from './manage-account/delete-account-confim-dialog/delete-account-confim-dialog.component';

const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  {
    path: 'accounts', component: ManageAccountComponent, children: [
      { path: 'edit/:accountId', component: EditAccountComponent },
      { path: 'delete/:accountId', component: DeleteAccountConfimDialogComponent },
      { path: 'show/:accountId', component: ShowAccountDetailComponent }
    ]
  },
  { path: 'accounts/add', component: AddAccountComponent },
  { path: 'verify/account/:key', component: VerifyAccountComponent },
  { path: '', redirectTo: '/accounts', pathMatch: 'full' },
  { path: '**', component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
