import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-lista',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista.html',
  styleUrls: ['./lista.css']
})
export class ListaComponent {
  itens = [
    { id: 1, nome: 'Item A' },
    { id: 2, nome: 'Item B' }
  ];
}
