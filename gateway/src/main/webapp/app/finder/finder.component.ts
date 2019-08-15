import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-finder',
  templateUrl: './finder.component.html',
  styleUrls: ['./finder.component.scss']
})
export class FinderComponent implements OnInit {

 qrResultString: string;

  constructor() { }

  ngOnInit() {
  }

  onCodeResult(resultString) {
    console.log(resultString);
    this.qrResultString = resultString;
  }

  /*clearResult(): void {
    this.qrResultString = null;
  }*/

}
