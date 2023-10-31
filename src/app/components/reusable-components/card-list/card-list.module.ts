import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardListComponent } from './card-list.component';



@NgModule({
  declarations: [CardListComponent],
  exports: [CardListComponent],
  imports: [
    CommonModule
  ]
})
export class CardListModule { }
