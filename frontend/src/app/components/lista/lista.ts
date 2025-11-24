import { Component } from '@angular/core';

@Component({
  selector: 'app-lista',
  templateUrl: './lista.html',
  styleUrls: ['./lista.css']
})
export class ListaComponent {
  itens = [
    { id: 1, nome: 'Item A' },
    { id: 2, nome: 'Item B' }
  ];
}
