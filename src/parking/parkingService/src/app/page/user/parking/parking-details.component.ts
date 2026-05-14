import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';

interface Espacio {
  id: number;
  relativeName: string;
  isOccupied: boolean;
  isReserved: boolean;
  tipoVehiculo: string;
}

interface Vehiculo {
  id: number;
  placa: string;
  tipoVehiculo: string;
  propietario: string;
}

@Component({
  selector: 'app-parking',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './parking-details.component.html',
  styleUrls: ['./parking-details.component.css']
})
export class ParkingComponent implements OnInit {

  espacios: Espacio[] = [];
  vehiculos: Vehiculo[] = [];

  mostrarModal: boolean = false;

  espacioSeleccionado?: Espacio;
  vehiculoSeleccionado?: number;

  fechaReserva?: string;
  fechaSalida?: string;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.cargarEspacios();
    this.cargarVehiculos();
  }

  obtenerToken(): string | null {
    return localStorage.getItem('token');
  }

  crearHeaders(): HttpHeaders {
    const token = this.obtenerToken();
    return new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  cargarEspacios(): void {
    this.http.get<Espacio[]>(
      'http://localhost:9000/api/space/all',
      { headers: this.crearHeaders() }
    ).subscribe(data => {
      this.espacios = data;
    });
  }

  cargarVehiculos(): void {
    const userId = localStorage.getItem('userId'); // ← clave correcta

    if (!userId) {
      console.error('No hay usuario logueado');
      return;
    }

    this.http.get<any[]>(
      `http://localhost:9000/api/vehiculo/persona/${userId}`,
      { headers: this.crearHeaders() }
    ).subscribe(data => {
      this.vehiculos = data.map(item => ({
        id: item[0],
        tipoVehiculo: item[1],
        placa: item[2],
        propietario: item[3]
      }));
    }, error => {
      console.error('Error cargando vehículos:', error);
    });
  }

  espaciosPorTipo(tipo: string): Espacio[] {
    return this.espacios.filter(e => e.tipoVehiculo === tipo);
  }

  abrirModal(espacio: Espacio): void {
    if (!espacio.isOccupied && !espacio.isReserved) {
      this.espacioSeleccionado = espacio;
      this.mostrarModal = true;
    }
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.espacioSeleccionado = undefined;
    this.vehiculoSeleccionado = undefined;
    this.fechaReserva = undefined;
    this.fechaSalida = undefined;
  }

  reservarEspacio(): void {

    // Validación: salida debe ser posterior a entrada
    if (this.fechaReserva && this.fechaSalida &&
        new Date(this.fechaSalida) <= new Date(this.fechaReserva)) {
      Swal.fire({
        title: 'Fechas inválidas',
        text: 'La hora de salida debe ser posterior a la hora de entrada.',
        icon: 'warning',
        confirmButtonText: 'Entendido'
      });
      return;
    }

    if (
      this.espacioSeleccionado &&
      this.vehiculoSeleccionado &&
      this.fechaReserva &&
      this.fechaSalida
    ) {
      const reserva = {
        space:    { id: this.espacioSeleccionado.id },
        vehiculo: { id: this.vehiculoSeleccionado },
        fechaReserva: this.fechaReserva,
        fechaSalida:  this.fechaSalida
      };

      this.http.post(
        'http://localhost:9000/api/reserva/add',
        reserva,
        { headers: this.crearHeaders() }
      ).subscribe(() => {

        this.actualizarIsReserved(this.espacioSeleccionado!.id, true);

        Swal.fire({
          title: '¡Reserva realizada con éxito!',
          icon: 'success',
          confirmButtonText: 'Aceptar'
        });

        this.cargarEspacios();
        this.cerrarModal();
      }, () => {
        Swal.fire({
          title: 'Error',
          text: 'No se pudo completar la reserva. Intenta de nuevo.',
          icon: 'error',
          confirmButtonText: 'Aceptar'
        });
      });
    }
  }

  actualizarIsReserved(id: number, isReserved: boolean): void {
    this.http.patch(
      `http://localhost:9000/api/space/update/reserved/${id}`,
      { isReserved },
      { headers: this.crearHeaders() }
    ).subscribe(
      () => this.cargarEspacios(),
      error => console.error('Error al actualizar isReserved:', error)
    );
  }
}