import {Component} from '@angular/core';
import {MatTableModule} from '@angular/material/table';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {NgIf, NgFor} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import { HttpClient } from '@angular/common/http';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';

export interface Author {
  name: string;
  key: string;
}

export interface Book {
  id: string;
  title: string;
  authors: Author[];
  coverImage: number | null
}

export interface SearchResults {
  books: Book[]
  retrievalTime: number
}

export interface SearchModel {
  searchTerm: string;
  loading: Boolean;
  lastDownload: string | null
}

@Component({
  selector: 'app-download-grid',
  templateUrl: './download-grid.component.html',
  styleUrls: ['./download-grid.component.css'],
  standalone: true,
  imports: [MatTableModule, MatFormFieldModule, MatInputModule, FormsModule, NgIf, NgFor, MatButtonModule, MatIconModule, MatProgressSpinnerModule]
})
export class DownloadGridComponent {

  constructor(private http: HttpClient) { }

  model: SearchModel = {searchTerm: "", loading: false, lastDownload: null};
  dataSource: Book[] = [];
  displayedColumns: string[] = ['key', 'title', 'authors'];

  load() {
    this.model.lastDownload = null;
    this.model.loading = true;
    this.http.get<SearchResults>('/api/search?q=' + encodeURIComponent(this.model.searchTerm)).subscribe(data => {
      this.dataSource = data.books;
      this.model.loading = false;
      this.model.lastDownload = this.millisecondsToStr(Date.now() - data.retrievalTime)
    })
  }

  millisecondsToStr(milliseconds: number) {
    // TIP: to find current time in milliseconds, use:
    // var  current_time_milliseconds = new Date().getTime();

    if (milliseconds < 1) {
      return 'less than a second ago'; //'just now' //or other string you like;
    }

    function numberEnding (number: number) {
      return (number > 1) ? 's' : '';
    }

    var temp = Math.floor(milliseconds / 1000);
    var minutes = Math.floor((temp %= 3600) / 60);
    if (minutes) {
      return minutes + ' minute' + numberEnding(minutes) + ' ago';
    }
    var seconds = temp % 60;
    if (seconds) {
      return seconds + ' second' + numberEnding(seconds) + ' ago';
    }
    return 'less than a second ago'; //'just now' //or other string you like;
  }
}
