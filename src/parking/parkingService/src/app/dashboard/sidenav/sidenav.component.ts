import { animate, keyframes, style, transition, trigger } from '@angular/animations';
import { Component, Output, EventEmitter, OnInit, HostListener } from '@angular/core';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { fadeInOut, INavbarData } from './helper';
import { navbarData } from './nav-data';
import { SublevelMenuComponent } from './sublevel-menu.component';
import { AuthService } from '../../services/auth.service';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-sidenav',
  standalone: true,
  imports: [CommonModule, RouterModule, SublevelMenuComponent, RouterLink],
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.css']
})
export class SidenavComponent implements OnInit {
  @Output() onToggleSideNav: EventEmitter<SideNavToggle> = new EventEmitter();
  collapsed = false;
  screenWidth = 0;
  navData: INavbarData[] = [];
  multiple: boolean = false;

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.screenWidth = window.innerWidth;
    if (this.screenWidth <= 768) {
      this.collapsed = false;
      this.onToggleSideNav.emit({ collapsed: this.collapsed, screenWidth: this.screenWidth });
    }
  }

  constructor(public router: Router, private authService: AuthService) { }

  ngOnInit(): void {
    this.screenWidth = window.innerWidth;
    this.loadNavData();
  }

  loadNavData(): void {
    const isAdmin = this.authService.isAdmin();
    // Rutas exclusivas de admin
    const adminOnlyRoutes = ['Admin', 'admin/facturacion'];

    if (isAdmin) {
      this.navData = navbarData;
    } else {
      // Filtrar items que son solo para admin
      this.navData = navbarData.filter(item => !adminOnlyRoutes.includes(item.routeLink));
    }
  }

  toggleCollapse(): void {
    this.collapsed = !this.collapsed;
    this.onToggleSideNav.emit({ collapsed: this.collapsed, screenWidth: this.screenWidth });
  }

  closeSidenav(): void {
    this.collapsed = false;
    this.onToggleSideNav.emit({ collapsed: this.collapsed, screenWidth: this.screenWidth });
  }

  handleClick(item: INavbarData): void {
    this.shrinkItems(item);
    item.expanded = !item.expanded;
  }

  getActiveClass(data: INavbarData): string {
    return this.router.url.includes(data.routeLink) ? 'active' : '';
  }

  shrinkItems(item: INavbarData): void {
    if (!this.multiple) {
      for (let modelItem of this.navData) {
        if (item !== modelItem && modelItem.expanded) {
          modelItem.expanded = false;
        }
      }
      if (!this.collapsed) {
        this.toggleCollapse();
      }
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/home'], { replaceUrl: true });
  }
}