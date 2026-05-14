import { Routes } from '@angular/router';
import { HomeComponent } from './page/home/home.component';
import { LoginComponent } from './auth/login/login.component';
import { ReservaListComponent } from './page/admin/reserva-list/reserva-list.component';
import { UserProfileComponent } from './page/user/profile/user-profile.component';
import { DashboardComponent } from './dashboard/dashboard/dashboard.component';
import { CommentsComponent } from './page/user/comments/comments.component';
import { VehiculosComponent as UserVehiculos } from './page/user/vehiculos/vehiculos.component';
import { VehiculosComponent as AdminVehiculos } from './page/admin/vehiculos/vehiculos.component';
import { PersonaListComponent } from './page/admin/persona-list/persona-list.component';
import { ParkingComponent } from './page/user/parking/parking-details.component';
import { SpacesComponent } from './page/admin/spaces/spaces.component';
import { FacturacionComponent } from './auth/facturacion/facturacion.component';


export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    children: [
      // user
      { path: 'profile', component: UserProfileComponent },
      { path: 'user/vehiculos', component: UserVehiculos },
      // admin
      { path: 'admin/vehiculos', component: AdminVehiculos },
      { path: 'admin/reservalist', component: ReservaListComponent },
      { path: 'admin/personalist', component: PersonaListComponent },
      { path: 'admin/spaces', component: SpacesComponent },
      { path: 'admin/facturacion', component: FacturacionComponent },
      // parking general
      { path: 'parking', component: ParkingComponent},
      { path: 'comments', component: CommentsComponent },
      { path: '**', redirectTo: 'parking', pathMatch: 'full' },
      // pasarela de compras
      
    ]
  },
  { path: 'auth', component: LoginComponent },
  { path: '**', redirectTo: '', pathMatch: 'full' }
];
