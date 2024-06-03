import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeFriendlistComponent } from './home-friendlist/home-friendlist.component';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
  { path: 'friendlist', component: HomeFriendlistComponent },
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
