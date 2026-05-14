import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  photo: string | null = null;
  fullName: string = 'unknownuser';
  email: string = 'unknownuser@gmail.com';
  role: string = 'User';
  password: string = '';
  location: string = 'location';
  notifications: boolean = false;
  isModalOpen: boolean = false;
  reservations: any[] = [];

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.loadUserProfile();
    this.loadUserReservations();
  }

  crearHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');

    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  loadUserProfile() {

    const userId = localStorage.getItem('userId');

    if (userId) {

      this.http.get<any>(
        `http://localhost:9000/api/auth/${userId}`,
        { headers: this.crearHeaders() }
      ).subscribe(

        (data) => {

          this.photo = this.isValidBase64(data.photo)
            ? data.photo
            : null;

          this.fullName = data.fullname;
          this.email = data.email;
          this.role = data.role;
          this.location = data.locate;
          this.password = data.password;
          this.notifications = data.notifications;
        },

        (error) => {

          Swal.fire(
            'Error',
            'Error al cargar los datos del usuario',
            'error'
          );

          console.error(error);
        }
      );
    }
  }

  openModal() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
  }

  // ✅ SOLO RESERVAS DEL USUARIO LOGUEADO
  loadUserReservations() {

    const userId = localStorage.getItem('userId');

    if (userId) {

      this.http.get<any[]>(
        `http://localhost:9000/api/reserva/persona/${userId}`,
        { headers: this.crearHeaders() }
      ).subscribe(

        (data) => {

          this.reservations = data;

          console.log('Reservas usuario:', this.reservations);
        },

        (error) => {

          Swal.fire(
            'Error',
            'Error al cargar las reservas',
            'error'
          );

          console.error(error);
        }
      );
    }
  }

  // Convertir imagen a Base64
  convertPhotoToBase64(event: Event) {

    const file = (event.target as HTMLInputElement).files?.[0];

    if (file) {

      const reader = new FileReader();

      reader.onload = () => {

        const base64String = reader.result as string;

        if (this.isValidBase64(base64String)) {
          this.photo = base64String;
        } else {
          this.photo = null;
        }
      };

      reader.readAsDataURL(file);
    }
  }

  isValidBase64(base64String: string): boolean {

    const regex = /^data:image\/[a-zA-Z]+;base64,[A-Za-z0-9+/=]+$/;

    return regex.test(base64String);
  }

  // Guardar cambios
  saveProfile() {

    const userId = localStorage.getItem('userId');

    if (userId) {

      const updatedProfile = {

        fullname: this.fullName,
        email: this.email,
        password: this.password,
        role: this.role,
        locate: this.location,
        notifications: this.notifications,
        photo: this.photo
      };

      this.http.put<any>(
        `http://localhost:9000/api/auth/a/${userId}`,
        updatedProfile,
        { headers: this.crearHeaders() }
      ).subscribe(

        (response) => {

          if (response.status === "success") {

            Swal.fire(
              'Éxito',
              'Perfil actualizado con éxito',
              'success'
            );

          } else {

            Swal.fire(
              'Error',
              'Error al actualizar el perfil',
              'error'
            );
          }
        },

        (error) => {

          Swal.fire(
            'Error',
            'Error al actualizar el perfil',
            'error'
          );
        }
      );
    }
  }
}