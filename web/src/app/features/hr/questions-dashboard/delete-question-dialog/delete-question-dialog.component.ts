import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-delete-question-dialog',
  templateUrl: './delete-question-dialog.component.html',
  styleUrls: ['./delete-question-dialog.component.scss']
})
export class DeleteQuestionDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<DeleteQuestionDialogComponent>) { }

  ngOnInit(): void {
  }

}
