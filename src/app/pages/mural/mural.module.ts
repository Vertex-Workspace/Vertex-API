import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MuralComponent } from './mural.component';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'src/app/components/reusable-components/button/button.module';



@NgModule({
  declarations: [MuralComponent],
  exports: [MuralComponent],
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule
  ]
})
export class MuralModule { }
