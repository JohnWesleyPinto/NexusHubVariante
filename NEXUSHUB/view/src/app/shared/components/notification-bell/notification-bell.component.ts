import { Component, inject, signal, OnInit, OnDestroy, ElementRef, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { NotificationService, NotificationItem } from '../../../core/services/notification.service';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-notification-bell',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './notification-bell.component.html',
  styleUrl: './notification-bell.component.css'
})
export class NotificationBellComponent implements OnInit, OnDestroy {
  protected readonly notificationService = inject(NotificationService);
  protected readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly elementRef = inject(ElementRef);

  protected readonly isOpen = signal(false);
  private pollInterval: any;

  ngOnInit() {
    if (this.authService.isLoggedIn()) {
      this.fetchNotifications();
      // Auto-refresh notifications every 30 seconds
      this.pollInterval = setInterval(() => {
        this.fetchNotifications();
      }, 30000);
    }
  }

  ngOnDestroy() {
    if (this.pollInterval) {
      clearInterval(this.pollInterval);
    }
  }

  fetchNotifications() {
    if (this.authService.isLoggedIn()) {
      this.notificationService.loadNotifications().subscribe({
        error: (err: any) => console.error('Erro ao buscar notificações', err)
      });
    }
  }

  toggleDropdown(event: Event) {
    event.stopPropagation();
    this.isOpen.update((open) => !open);
    if (this.isOpen()) {
      this.fetchNotifications();
    }
  }

  closeDropdown() {
    this.isOpen.set(false);
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event) {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.closeDropdown();
    }
  }

  onMarkAllAsRead(event: Event) {
    event.stopPropagation();
    this.notificationService.markAllAsRead().subscribe();
  }

  onNotificationClick(item: NotificationItem) {
    if (!item.isRead) {
      this.notificationService.markAsRead(item.id).subscribe();
    }
    this.closeDropdown();

    if (item.link) {
      this.router.navigateByUrl(item.link);
    }
  }

  getIconForType(type: string): string {
    switch (type) {
      case 'OPPORTUNITY_APPLICATION':
        return 'assignment_ind';
      case 'OPPORTUNITY_NEW':
        return 'campaign';
      case 'STORE_METRIC':
        return 'trending_up';
      case 'STORE_REMINDER':
        return 'storefront';
      case 'SYSTEM_NOTICE':
      default:
        return 'notifications';
    }
  }

  getRelativeTime(dateStr: string): string {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    const now = new Date();
    const seconds = Math.floor((now.getTime() - date.getTime()) / 1000);

    if (seconds < 60) return 'há alguns segundos';
    const minutes = Math.floor(seconds / 60);
    if (minutes < 60) return `há ${minutes} min`;
    const hours = Math.floor(minutes / 60);
    if (hours < 24) return `há ${hours} h`;
    const days = Math.floor(hours / 24);
    return `há ${days} d`;
  }
}
