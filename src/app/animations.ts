import { animate, animateChild, group, query, style, transition, trigger } from "@angular/animations";

export const slideInAnimation =
  trigger('routeAnimations', [
    
    //Animação do Cadastro pro Login
    transition('registerPage => loginPage', [
      style({ position: 'relative' }),
      query(':enter, :leave', [
        style({
          position: 'fixed',
          top: 0,
          left: 0,
          width: '100%'
        })
      ]),
      query(':enter', [
        style({ left: '-100%' })
      ], { optional: true }),
      query(':leave', animateChild(), { optional: false }),
      group([
        query(':leave', [
          animate('300ms ease-out', style({ left: '100%'}))
        ], { optional: true }),
        query(':enter', [
          animate('300ms ease-out', style({ left: '0%' }))
        ], { optional: true }),
      ]),
    ]),

    //Animação do Login pro Cadastro
    transition('loginPage => registerPage', [
      style({ position: 'relative' }),
      query(':enter, :leave', [
        style({
          position: 'fixed',
          top: 0,
          right: 0,
          width: '100%'
        })
      ], { optional: true }),
      query(':enter', [
        style({ right: '-100%' })
      ], { optional: true }),
      query(':leave', animateChild(), { optional: true }),
      group([
        query(':leave', [
          animate('300ms ease-out', style({ right: '100%' }))
        ], { optional: true }),
        query(':enter', [
          animate('300ms ease-out', style({ right: '0%' }))
        ], { optional: true }),
        query('@*', animateChild(), { optional: true })
      ]),
    ]),

    //Animação dos components, muda opacidade
    transition('loginPage => muralPage', [
      style({ position: 'relative' }),
      query(':enter, :leave', [
        style({
          width: '100%'
        })
      ], { optional: true }),
      query(':enter', [
        style({ opacity: '0%' })
      ], { optional: true }),
      query(':leave', animateChild(), { optional: true }),
      group([
        query(':leave', [
          animate('300ms ease-out', style({ opacity: '0%' }))
        ], { optional: true }),
        query(':enter', [
          animate('300ms ease-out', style({ opacity: '100%' }))
        ], { optional: true }),
        query('@*', animateChild(), { optional: true })
      ]),
    ])
  ]);

  
  