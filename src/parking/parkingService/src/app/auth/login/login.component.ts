import { Component } from '@angular/core';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { RouterModule, Router, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, RouterModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loading = false;
  errorMessage: string | null = null;
  successMessage: string | null = null;
  isLoginMode = true;

  constructor(private http: HttpClient, private router: Router) { }

  login(loginForm: NgForm) {
    if (!loginForm.valid) return;

    this.loading = true;
    this.errorMessage = null;
    this.successMessage = null;

    const payload = {
      email: loginForm.value.email,
      password: loginForm.value.password,
    };

    this.http.post('http://localhost:9000/api/auth/auth', payload)
      .subscribe({
        next: (data: any) => {
          this.loading = false;
          if (data.estado) {
            localStorage.setItem('token', data.token);
            localStorage.setItem('userId', data.id);
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('user', JSON.stringify(data));
            localStorage.setItem('role', data.role);
            this.router.navigate(['/dashboard']);
          } else {
            this.errorMessage = data.message || 'Email o contraseña incorrectos';
            localStorage.setItem('isLoggedIn', 'false');
          }
        },
        error: (error) => {
          this.loading = false;
          if (error.status === 401 || error.status === 403) {
            this.errorMessage = '📧 Tu cuenta aún no está verificada. Revisa tu correo electrónico.';
          } else {
            this.errorMessage = error.error?.message || 'Ocurrió un error al iniciar sesión.';
          }
          localStorage.setItem('isLoggedIn', 'false');
        }
      });
  }

  register(registerForm: NgForm) {
    if (!registerForm.valid) return;

    this.loading = true;
    this.errorMessage = null;
    this.successMessage = null;

    const payload = {
      photo: 'https://example.com/photo.jpg',
      fullname: registerForm.value.fullname,
      password: registerForm.value.password,
      email: registerForm.value.email,
      role: registerForm.value.role,
      locate: registerForm.value.locate,
      last_login: new Date().toISOString(),
      created_at: new Date().toISOString(),
      updated_at: new Date().toISOString(),
      status: false,
      notifications: false
    };

    this.http.post('http://localhost:9000/api/auth/add', payload)
      .subscribe({
        next: (data: any) => {
          this.loading = false;
          if (data.estado) {
            this.successMessage = `✅ ¡Registro exitoso! Enviamos un correo de verificación a ${registerForm.value.email}. Revisa tu bandeja de entrada y haz clic en el enlace para activar tu cuenta.`;
            registerForm.resetForm();
          } else {
            this.errorMessage = data.mensaje || 'Error al registrar el usuario.';
          }
        },
        error: (error) => {
          this.loading = false;
          // ✅ Aquí capturamos el mensaje del backend correctamente
          // El backend devuelve { estado: false, mensaje: "El correo ya está registrado" }
          if (error.error?.mensaje) {
            this.errorMessage = error.error.mensaje;
          } else if (error.status === 409) {
            this.errorMessage = '⚠️ Este correo ya está registrado. Intenta iniciar sesión.';
          } else {
            this.errorMessage = 'Ocurrió un error al registrar. Intenta de nuevo.';
          }
        }
      });
  }

  toggleMode() {
    this.isLoginMode = !this.isLoginMode;
    this.errorMessage = null;
    this.successMessage = null;
  }
}