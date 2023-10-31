import { Component } from '@angular/core';
import { PersonalizationService } from 'src/app/services/personalization.service';


@Component({
  selector: 'app-mural',
  templateUrl: './mural.component.html',
  styleUrls: ['./mural.component.scss']
})
export class MuralComponent {

  primaryColor: string;
  secondColor: string;
  constructor(private personalization : PersonalizationService){
    this.primaryColor = personalization.getPrimaryColor();
    this.secondColor = personalization.getSecondColor();
  }


  teste():void{
    console.log("teste muasdsral");
  }
}
