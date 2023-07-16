import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DownloadGridComponent } from './download-grid.component';

describe('DownloadGridComponent', () => {
  let component: DownloadGridComponent;
  let fixture: ComponentFixture<DownloadGridComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DownloadGridComponent]
    });
    fixture = TestBed.createComponent(DownloadGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
