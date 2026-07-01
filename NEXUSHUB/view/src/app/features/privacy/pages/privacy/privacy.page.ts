import { Component,inject,signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { apiUrl } from '../../../../core/config/api.config';
@Component({selector:'app-privacy-page',standalone:true,imports:[CommonModule],templateUrl:'./privacy.page.html',styleUrl:'./privacy.page.css'})
export class PrivacyPageComponent{
 private readonly http=inject(HttpClient);private readonly base=apiUrl('/api/lgpd');
 readonly data=signal<unknown>(null);readonly message=signal('');
 consent(granted:boolean){this.http.post(`${this.base}/consentimentos`,{purpose:'PLATFORM_ANALYTICS',version:'1.0',granted}).subscribe(()=>this.message.set('Preferência de consentimento registrada.'));}
 export(){this.http.get(`${this.base}/meus-dados`).subscribe(v=>this.data.set(v));}
 anonymize(){this.http.post(`${this.base}/solicitacoes`,{type:'ANONYMIZE'}).subscribe(()=>this.message.set('Solicitação de anonimização registrada para análise.'));}
}
