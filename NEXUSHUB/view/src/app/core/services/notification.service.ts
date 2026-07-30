import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { apiUrl } from '../config/api.config';
import { Observable, tap } from 'rxjs';

export interface NotificationItem {
  id: string;
  title: string;
  message: string;
  type: 'OPPORTUNITY_APPLICATION' | 'OPPORTUNITY_NEW' | 'STORE_METRIC' | 'STORE_REMINDER' | 'SYSTEM_NOTICE' | string;
  link?: string;
  isRead: boolean;
  sendEmail: boolean;
  emailSent: boolean;
  createdAt: string;
}

export interface NotificationResponse {
  notifications: NotificationItem[];
  unreadCount: number;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private readonly http = inject(HttpClient);

  readonly notifications = signal<NotificationItem[]>([]);
  readonly unreadCount = signal<number>(0);

  loadNotifications(): Observable<NotificationResponse> {
    return this.http.get<NotificationResponse>(apiUrl('/api/notifications')).pipe(
      tap((res) => {
        this.notifications.set(res.notifications || []);
        this.unreadCount.set(res.unreadCount || 0);
      })
    );
  }

  markAsRead(id: string): Observable<NotificationItem> {
    return this.http.patch<NotificationItem>(apiUrl(`/api/notifications/${id}/read`), {}).pipe(
      tap((updated) => {
        this.notifications.update((list) =>
          list.map((item) => (item.id === id ? { ...item, isRead: true } : item))
        );
        this.unreadCount.update((c) => Math.max(0, c - 1));
      })
    );
  }

  markAllAsRead(): Observable<void> {
    return this.http.patch<void>(apiUrl('/api/notifications/read-all'), {}).pipe(
      tap(() => {
        this.notifications.update((list) =>
          list.map((item) => ({ ...item, isRead: true }))
        );
        this.unreadCount.set(0);
      })
    );
  }
}
