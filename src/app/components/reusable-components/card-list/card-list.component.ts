import { Component } from '@angular/core';

@Component({
  selector: 'app-card-list',
  templateUrl: './card-list.component.html',
  styleUrls: ['./card-list.component.scss']
})
export class CardListComponent {

  // it will be an input


  teams = [
    {image: '../../../../assets/teste/teste.jpg', name: 'AKMO', date: '06/06/2023', leader: 'Otávio Miguel Rocha'},
    {image: '../../../../assets/teste/teste.jpg', name: 'AKMO', date: '06/06/2023', leader: 'Otávio Miguel Rocha'},
    {image: '../../../../assets/teste/teste.jpg', name: 'AKMO', date: '06/06/2023', leader: 'Otávio Miguel Rocha'},
    {image: '../../../../assets/teste/teste.jpg', name: 'AKMO', date: '06/06/2023', leader: 'Otávio Miguel Rocha'},
    {image: '../../../../assets/teste/teste.jpg', name: 'AKMO', date: '06/06/2023', leader: 'Otávio Miguel Rocha'},
    {image: '../../../../assets/teste/teste.jpg', name: 'AKMO', date: '06/06/2023', leader: 'Otávio Miguel Rocha'},
    {image: '../../../../assets/teste/teste.jpg', name: 'AKMO', date: '06/06/2023', leader: 'Otávio Miguel Rocha'},
    {image: '../../../../assets/teste/teste.jpg', name: 'AKMO', date: '06/06/2023', leader: 'Otávio Miguel Rocha'},
    {image: '../../../../assets/teste/teste.jpg', name: 'AKMO', date: '06/06/2023', leader: 'Otávio Miguel Rocha'},
    {image: '../../../../assets/teste/teste.jpg', name: 'AKMO', date: '06/06/2023', leader: 'Otávio Miguel Rocha'},
  ];
}
