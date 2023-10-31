import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PropertiesComponent } from './properties.component';
import { EditPropertiesComponent } from './edit-properties/edit-properties.component';



@NgModule({
  declarations: [
    PropertiesComponent,
    EditPropertiesComponent
  ],
  exports: [
    PropertiesComponent
  ],
  imports: [
    CommonModule
  ]
})
export class PropertiesModule { }
