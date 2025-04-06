import { Component } from '@angular/core';
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {


  showNavbar: boolean = true;
  constructor(private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.showNavbar = !(event.urlAfterRedirects === '/' || event.urlAfterRedirects === '');

      }
    });
  }

}
