import { INavbarData } from './helper';

export const navbarData: INavbarData[] = [
  {
    routeLink: 'parking',
    icon: 'bi bi-car-front-fill',
    label: 'Parqueadero',
  },
  {
    routeLink: '',
    icon: 'bi bi-file-earmark-person',
    label: 'Usuario',
    items: [
      {
        routeLink: 'profile',
        label: 'Perfíl',
      },
      {
        routeLink: 'user/vehiculos',
        label: 'Mis vehiculos',
      },
    ],
  },
  {
    routeLink: 'comments',
    icon: 'bi bi-chat-left-text',
    label: 'Comentarios'
  },
  {
    routeLink: 'Admin',
    icon: 'bi bi-person-lock',
    label: 'Gestiónes administrativas',
    items: [
      {
        routeLink: 'admin/personalist',
        label: 'Usuarios registrados',
      },
      {
        routeLink: 'admin/vehiculos',
        label: 'Registro de vehiculos',
      },
      {
        routeLink: 'admin/spaces',
        label: 'Espacios del parqueadero',
      }
    ],
  },
  {
    routeLink: 'admin/facturacion',
    icon: 'bi bi-receipt',
    label: 'Facturación'
  },
];