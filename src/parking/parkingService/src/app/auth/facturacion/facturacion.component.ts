import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';

interface Factura {
  id: number;
  placa: string;
  tipoVehiculo: string;
  propietario: string;
  espacio: string;
  fechaEntrada: Date;
  fechaSalida: Date;
  horas: number;
  tarifa: number;
  total: number;
}

@Component({
  selector: 'app-facturacion',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: './facturacion.component.html',
  styleUrls: ['./facturacion.component.css']
})
export class FacturacionComponent implements OnInit {

  facturas: Factura[] = [];
  facturasFiltradas: Factura[] = [];

  busqueda: string = '';
  cargando: boolean = false;
  eliminando: boolean = false;
  error: string | null = null;
  facturaSeleccionada: Factura | null = null;

  readonly TARIFA_POR_HORA = 2000;

  private apiUrl    = 'http://localhost:9000/api/reserva/all';
  private deleteUrl = 'http://localhost:9000/api/reserva/delete';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.cargarFacturas();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({ 'Authorization': `Bearer ${token}` });
  }

  cargarFacturas(): void {
    this.cargando = true;
    this.error = null;

    this.http.get<any[]>(this.apiUrl, { headers: this.getHeaders() }).subscribe({
      next: (reservas) => {
        this.facturas = reservas
          .filter(r => r.fechaSalida)
          .map(r => this.calcularFactura(r));

        this.facturasFiltradas = [...this.facturas];
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar las facturas.';
        this.cargando = false;
      }
    });
  }

  private calcularFactura(reserva: any): Factura {
    const entrada = new Date(reserva.fechaReserva);
    const salida  = new Date(reserva.fechaSalida);
    const diffMs  = salida.getTime() - entrada.getTime();
    const horas   = Math.max(1, Math.ceil(diffMs / (1000 * 60 * 60)));
    const total   = horas * this.TARIFA_POR_HORA;

    return {
      id:           reserva.id,
      placa:        reserva.vehiculo?.placa || 'N/A',
      tipoVehiculo: reserva.vehiculo?.tipoVehiculo || 'N/A',
      propietario:  reserva.vehiculo?.persona?.fullname || 'N/A',
      espacio:      reserva.space?.relativeName || 'N/A',
      fechaEntrada: entrada,
      fechaSalida:  salida,
      horas,
      tarifa:       this.TARIFA_POR_HORA,
      total
    };
  }

  filtrar(): void {
    const q = this.busqueda.toLowerCase().trim();

    if (!q) {
      this.facturasFiltradas = [...this.facturas];
      return;
    }

    this.facturasFiltradas = this.facturas.filter(f =>
      f.placa.toLowerCase().includes(q) ||
      f.propietario.toLowerCase().includes(q) ||
      f.espacio.toLowerCase().includes(q)
    );
  }

  abrirFactura(factura: Factura): void {
    this.facturaSeleccionada = factura;
  }

  cerrarFactura(): void {
    if (!this.facturaSeleccionada || this.eliminando) return;

    const id = this.facturaSeleccionada.id;
    this.eliminando = true;

    this.http.delete(`${this.deleteUrl}/${id}`, { headers: this.getHeaders() }).subscribe({
      next: () => {
        // Eliminar de las listas locales sin recargar
        this.facturas          = this.facturas.filter(f => f.id !== id);
        this.facturasFiltradas = this.facturasFiltradas.filter(f => f.id !== id);
        this.facturaSeleccionada = null;
        this.eliminando = false;
      },
      error: () => {
        alert('Error al eliminar la reserva del sistema. Intenta de nuevo.');
        this.facturaSeleccionada = null;
        this.eliminando = false;
      }
    });
  }

  descargarPDF(): void {
    const DATA: any = document.getElementById('facturaPDF');

    html2canvas(DATA).then(canvas => {
      const imgWidth  = 208;
      const pageHeight = 295;
      const imgHeight = canvas.height * imgWidth / canvas.width;
      const contentDataURL = canvas.toDataURL('image/png');
      const pdf = new jsPDF('p', 'mm', 'a4');

      pdf.addImage(contentDataURL, 'PNG', 0, 0, imgWidth, imgHeight);
      pdf.save(`factura-${this.facturaSeleccionada?.placa}.pdf`);
    });
  }

  get totalRecaudado(): number {
    return this.facturasFiltradas.reduce((sum, f) => sum + f.total, 0);
  }

  get totalFacturas(): number {
    return this.facturasFiltradas.length;
  }

  get promedioHoras(): number {
    if (!this.facturasFiltradas.length) return 0;
    const total = this.facturasFiltradas.reduce((sum, f) => sum + f.horas, 0);
    return Math.round(total / this.facturasFiltradas.length);
  }

  formatCOP(valor: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0
    }).format(valor);
  }
}