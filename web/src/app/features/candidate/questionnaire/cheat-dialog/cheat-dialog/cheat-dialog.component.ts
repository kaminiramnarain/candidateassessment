import { Component, OnInit, Inject } from '@angular/core';
import { CheatDialogData } from 'src/app/shared/model';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-cheat-dialog',
  templateUrl: './cheat-dialog.component.html',
  styleUrls: ['./cheat-dialog.component.scss']
})
export class CheatDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<CheatDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: CheatDialogData) { }

  public ranking!: string;
  public disqualify: boolean = false;

  ngOnInit(): void {
    this.checkWarning();
  }

  public checkWarning(): void {
    if (this.data.cheat === 1)
      this.ranking = "st"
    if (this.data.cheat === 2)
      this.ranking = "nd"
    if (this.data.cheat === 3)
      this.ranking = "rd"
    if (this.data.cheat > 3) {
      this.ranking = "th"
      this.disqualify=true;
    }

  }


}
