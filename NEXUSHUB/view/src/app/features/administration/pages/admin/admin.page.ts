import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { apiUrl } from '../../../../core/config/api.config';
interface Flag{id:string;code:string;enabled:boolean;description:string}
interface Audit{action:string;entity:string;entityId:string;result:string;createdAt:string}
@Component({selector:'app-admin-page',standalone:true,imports:[CommonModule],templateUrl:'./admin.page.html',styleUrl:'./admin.page.css'})
export class AdminPageComponent{
 private readonly http=inject(HttpClient);private readonly base=apiUrl('/api/admin');
 readonly flags=signal<Flag[]>([]);readonly audits=signal<Audit[]>([]);readonly error=signal('');
 constructor(){this.refresh();}
 refresh(){this.http.get<Flag[]>(`${this.base}/features`).subscribe({next:v=>this.flags.set(v),error:()=>this.error.set('Acesso exclusivo para administradores.')});
  this.http.get<{content:Audit[]}>(`${this.base}/auditoria`).subscribe({next:v=>this.audits.set(v.content)});}
 toggle(flag:Flag){this.http.patch<Flag>(`${this.base}/features/${flag.code}`,{enabled:!flag.enabled}).subscribe(()=>this.refresh());}
}
