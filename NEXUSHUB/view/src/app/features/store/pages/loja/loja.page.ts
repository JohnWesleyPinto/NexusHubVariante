import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PaymentService, Product } from '../../services/payment.service';

@Component({
  selector: 'app-loja-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './loja.page.html',
  styleUrl: './loja.page.css'
})
export class LojaPageComponent {
 private readonly payment=inject(PaymentService);
 readonly products=signal<Product[]>([]);readonly message=signal('');readonly loading=signal(false);
 constructor(){this.payment.products().subscribe({next:value=>this.products.set(value),error:()=>this.message.set('Entre na sua conta para acessar a loja.')});}
 buy(product:Product){
  this.loading.set(true);this.message.set('');
  this.payment.checkout(product.id).subscribe({next:order=>{
   this.loading.set(false);
   if(order.checkoutUrl.startsWith('/api/')){this.payment.confirmFake(order.id).subscribe(()=>this.message.set('Pagamento sandbox confirmado com sucesso.'));}
   else window.location.href=order.checkoutUrl;
  },error:error=>{this.loading.set(false);this.message.set(error.error?.message??'Não foi possível iniciar o pagamento.');}});
 }
}
