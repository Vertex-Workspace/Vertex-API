import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskComponent } from './task.component';
import { CommentsComponent } from './comments/comments.component';
import { LogComponent } from './log/log.component';
import { PropertiesComponent } from './properties/properties.component';

@NgModule({
  declarations: [
    TaskComponent,
    CommentsComponent,
    LogComponent,
    PropertiesComponent
  ],
  exports: [TaskComponent],
  imports: [CommonModule],
})
export class TaskModule {}
