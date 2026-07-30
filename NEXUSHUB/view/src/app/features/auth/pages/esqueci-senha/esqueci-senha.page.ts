import { Component, signal, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../../core/auth/auth.service';
import { HttpClient } from '@angular/common/http';
import { apiUrl } from '../../../../core/config/api.config';

@Component({
  selector: 'app-esqueci-senha-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './esqueci-senha.page.html',
  styleUrl: './esqueci-senha.page.css'
})
export class EsqueciSenhaPageComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly http = inject(HttpClient);

  protected email = '';
  protected novaSenha = '';
  protected token = signal<string | null>(null);

  protected readonly isLoading = signal(false);
  protected readonly errorMessage = signal('');
  protected readonly successMessage = signal('');

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      if (params['token']) {
        this.token.set(params['token']);
      }
    });
  }

  onSubmit() {
    this.isLoading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    const tokenVal = this.token();

    if (tokenVal) {
      // Redefinição com Token único recebido por e-mail
      this.http.post(apiUrl('/api/usuarios/redefinir-senha-token'), {
        token: tokenVal,
        novaSenha: this.novaSenha
      }).subscribe({
        next: () => {
          this.isLoading.set(false);
          this.successMessage.set('Sua nova senha foi cadastrada com sucesso! Você já pode efetuar login.');
        },
        error: (err) => {
          this.isLoading.set(false);
          this.errorMessage.set(err.error?.message || 'Token de redefinição inválido, utilizado ou expirado.');
        }
      });
    } else {
      // Solicitação de envio de e-mail com Token único
      this.http.post(apiUrl('/api/usuarios/solicitar-codigo-recuperacao'), {
        email: this.email
      }).subscribe({
        next: () => {
          this.isLoading.set(false);
          this.successMessage.set('Um link com token único de redefinição de senha foi enviado para seu e-mail!');
        },
        error: (err) => {
          this.isLoading.set(false);
          this.errorMessage.set(err.error?.message || 'Falha ao solicitar código de recuperação. Verifique o e-mail informado.');
        }
      });
    }
  }
}


