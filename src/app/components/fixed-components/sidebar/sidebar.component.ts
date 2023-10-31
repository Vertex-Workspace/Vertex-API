import { Component } from '@angular/core';
import { faMessage, faMagnifyingGlass, faClipboardList, 
  faUser, faGear, faSignOut, faBellSlash } from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  faMessage = faMessage;
  faMagnifyingGlass = faMagnifyingGlass;
  faClipboardList = faClipboardList;
  faUser = faUser;
  faGear = faGear;
  faSignOut = faSignOut;

}
