import { Component, OnInit, Inject } from '@angular/core';
import { DialogData } from 'src/app/shared/model';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-marks-dialog',
  templateUrl: './marks-dialog.component.html',
  styleUrls: ['./marks-dialog.component.scss']
})
export class MarksDialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<MarksDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) { }

  ngOnInit(): void {
  }

}
