import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EditPropertiesComponent } from './edit-properties.component';



@NgModule({
  declarations: [EditPropertiesComponent],
  exports: [EditPropertiesComponent],
  imports: [
    CommonModule
  ]
})
export class EditPropertiesModule { }
