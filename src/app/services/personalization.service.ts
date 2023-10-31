import { Injectable } from '@angular/core';

interface Personalization {
  primaryColor: string;
  secondColor: string;
  tittleFont: string;
  textFont: string;
}

@Injectable({
  providedIn: 'root',
})
export class PersonalizationService {
  constructor() {}

  setPersonalization(): void {
    const defaultColors: Personalization = {
      primaryColor: '#092C4C',
      secondColor: '#F3F3F3',
      tittleFont: "'Manrope', sans-serif",
      textFont: "'Inter', sans-serif",
    };
    localStorage.setItem('personalization', JSON.stringify(defaultColors));
  }

  getPrimaryColor(): string {
    return JSON.parse(localStorage.getItem('personalization')!).primaryColor;
  }

  getSecondColor(): string {
    return JSON.parse(localStorage.getItem('personalization')!).secondColor;
  }

  getTittleFont(): string {
    return JSON.parse(localStorage.getItem('personalization')!).tittleFont;
  }

  getTextFont(): string {
    return JSON.parse(localStorage.getItem('personalization')!).textFont;
  }
}
