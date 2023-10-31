import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeamsComponent } from './teams.component';
import { CardListModule } from 'src/app/components/reusable-components/card-list/card-list.module';
import { ChartModule } from 'primeng/chart';
import { QuickAccessModule } from 'src/app/components/reusable-components/quick-access/quick-access.module';


@NgModule({
  declarations: [TeamsComponent],
  exports:[TeamsComponent],
  imports: [
    CommonModule,
    CardListModule,
    QuickAccessModule
  ],
})
export class TeamsModule { }
