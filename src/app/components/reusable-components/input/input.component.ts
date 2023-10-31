import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-input',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.scss']
})
export class InputComponent {

  @Input()
  width!: string;

  @Input()
  height!: string;

  @Input()
  backgroundColor!: string;

  @Input()
  placeHolder!: string;
}
