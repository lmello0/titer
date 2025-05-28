import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { SignInComponent } from '../../../sign-in/sign-in.component';
import { CreateAccountComponent } from '../../../create-account/create-account.component';

@Component({
  selector: 'app-login-to-review-it',
  imports: [RouterLink, SignInComponent, CreateAccountComponent],
  templateUrl: './login-to-review-it.component.html',
  styleUrl: './login-to-review-it.component.css',
})
export class LoginToReviewItComponent {
  isLoginOpen = false;
  isCreateAccountOpen = false;
}
