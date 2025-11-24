import { Component, OnInit, inject } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../services/auth.models';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  loginForm!: FormGroup;
  submitted = false;
  loading = false;
  errorMessage = '';

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const loginRequest: LoginRequest = {
      username: this.f['username'].value,
      password: this.f['password'].value
    };

    this.authService.login(loginRequest).subscribe({
      next: (response: any) => {
        this.loading = false;
        // Redirect to dashboard or home
        this.router.navigate(['/dashboard']);
      },
      error: (error: any) => {
        this.loading = false;
        this.errorMessage = error.message || 'Login failed. Please check your credentials.';
        console.error('Login error:', error);
      }
    });
  }
}
