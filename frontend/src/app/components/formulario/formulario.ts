import { Component } from '@angular/core';

@Component({
  selector: 'app-formulario',
  templateUrl: './formulario.html',
  styleUrls: ['./formulario.css']
})
export class FormularioComponent {
  nome: string = '';

  salvar() {
    console.log('Salvando item:', this.nome);
  }
}
