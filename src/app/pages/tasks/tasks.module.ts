import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TasksComponent } from './tasks.component';
import { ListComponent } from './list/list.component';



@NgModule({
  declarations: [TasksComponent, ListComponent],
  exports:[TasksComponent],
  imports: [
    CommonModule
  ],
})
export class TasksModule { }
