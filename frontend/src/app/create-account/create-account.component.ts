import {
  Component,
  EventEmitter,
  inject,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { ModalComponent } from '../shared/components/modal/modal.component';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-create-account',
  imports: [CommonModule, ModalComponent, ReactiveFormsModule],
  templateUrl: './create-account.component.html',
  styleUrl: './create-account.component.css',
})
export class CreateAccountComponent {
  private formBuilder = inject(FormBuilder);

  @Input() isCreateAccountOpen = false;
  @Output() onClose = new EventEmitter<void>();

  isLoading = false;

  createAccountForm = this.formBuilder.group({
    username: [
      '',
      [Validators.required, Validators.minLength(4), Validators.maxLength(25)],
    ],
    email: ['', Validators.required, Validators.email],
    password: [
      '',
      [
        Validators.required,
        Validators.minLength(12),
        Validators.maxLength(32),
        Validators.pattern(
          /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()\-_=\+\[\]{};:]).{8,32}$/,
        ),
      ],
    ],
    confirmPassword: [
      '',
      [
        Validators.required,
        Validators.minLength(12),
        Validators.maxLength(32),
        Validators.pattern(
          /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()\-_=\+\[\]{};:]).{8,32}$/,
        ),
      ],
    ],
    termsOfUse: [false, Validators.requiredTrue],
    privacyPolicy: [false, Validators.requiredTrue],
  });

  submit(): void {
    if (this.createAccountForm.invalid) return;

    this.isLoading = true;

    const {
      username,
      email,
      password,
      confirmPassword,
      termsOfUse,
      privacyPolicy,
    } = this.createAccountForm.value;

    setTimeout(() => {
      console.log('Account created!');

      this.isLoading = false;
      this.closeModal();
    }, 1_000);
  }

  closeModal(): void {
    if (this.isLoading) return;

    this.isCreateAccountOpen = false;
    this.createAccountForm.reset();
    this.onClose.emit();
  }

  isPasswordsEqual(): boolean {
    const password = this.createAccountForm.get('password');
    const confirmPassword = this.createAccountForm.get('confirmPassword');

    return (
      !!password?.value &&
      !!confirmPassword?.value &&
      password?.value === confirmPassword?.value
    );
  }

  getError(controlName: string, error: string): boolean {
    const control = this.createAccountForm.get(controlName);

    return !!(control && control.touched && control.hasError(error));
  }
}
