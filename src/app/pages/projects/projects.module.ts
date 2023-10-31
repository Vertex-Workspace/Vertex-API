import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectsComponent } from './projects.component';



@NgModule({
  declarations: [ProjectsComponent],
  exports:[ProjectsComponent],
  imports: [
    CommonModule
  ],
})
export class ProjectsModule { }
