import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserSettingsComponent } from './user-settings.component';



@NgModule({
  declarations: [
    UserSettingsComponent
  ],
  exports: [
    UserSettingsComponent
  ],
  imports: [
    CommonModule
  ]
})
export class UserSettingsModule { }
