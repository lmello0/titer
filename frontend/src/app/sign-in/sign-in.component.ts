import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { ModalComponent } from '../shared/components/modal/modal.component';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-sign-in',
  imports: [CommonModule, ModalComponent, ReactiveFormsModule, RouterLink],
  templateUrl: './sign-in.component.html',
  styleUrl: './sign-in.component.css',
})
export class SignInComponent {
  private formBuilder = inject(FormBuilder);

  @Input() isLoginOpen = false;
  @Output() onClose = new EventEmitter<void>();

  isLoading = false;
  errorMessage = '';

  loginForm = this.formBuilder.group({
    username: [
      '',
      [Validators.required, Validators.minLength(4), Validators.maxLength(25)],
    ],
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
  });

  submit(): void {
    if (this.loginForm.invalid) return;

    this.isLoading = true;
    this.errorMessage = '';

    const { username, password } = this.loginForm.value;

    // TODO: add login logic
    setTimeout(() => {
      console.log('Login successful!');

      this.isLoading = false;
      this.closeModal();
    }, 10_000);
  }

  closeModal(): void {
    if (this.isLoading) return;

    this.isLoginOpen = false;
    this.loginForm.reset();
    this.onClose.emit();
  }

  getError(controlName: string, error: string): boolean {
    const control = this.loginForm.get(controlName);

    return !!(control && control.touched && control.hasError(error));
  }
}
