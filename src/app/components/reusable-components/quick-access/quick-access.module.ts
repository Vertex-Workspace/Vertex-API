import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChartModule } from 'primeng/chart';
import { QuickAccessComponent } from './quick-access.component';



@NgModule({
  declarations: [QuickAccessComponent],
  exports: [QuickAccessComponent],
  imports: [
    CommonModule,
    ChartModule
  ]
})
export class QuickAccessModule { }
