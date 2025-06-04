import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { SearchInputComponent } from '../search-input/search-input.component';
import { Option } from '../search-input/interface/option.interface';
import { debounceTime, distinctUntilChanged, Subject, takeUntil } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-search-input-with-field',
  imports: [CommonModule, ReactiveFormsModule, SearchInputComponent],
  templateUrl: './search-input-with-field.component.html',
  styleUrl: './search-input-with-field.component.css',
})
export class SearchInputWithFieldComponent implements OnInit, OnDestroy {
  @Input() options: Option[] = [];
  @Input() searchPlaceholder: string = 'Search...';
  @Input() selectedValue: any = null;
  @Input() allowAddNew: boolean = false;
  @Input() addNewText: string = 'Add new option';

  @Output() selectionChange = new EventEmitter<Option | null>();

  @Input() label: string = 'As:';
  @Input() addPlaceholder: string = 'Enter new value...';

  @Output() fieldChange: EventEmitter<string | null> = new EventEmitter<string | null>();

  fieldControl: FormControl = new FormControl('');

  private destroy$: Subject<void> = new Subject<void>();

  showField: boolean = false;

  ngOnInit(): void {
    this.fieldControl.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe((inputValue) => this.fieldChange.emit(inputValue));
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onSelectionChange(value: Option | null) {
    if (!value) {
      this.showField = true;
    } else {
      this.showField = false;
    }

    this.selectionChange.emit(value);
  }
}
