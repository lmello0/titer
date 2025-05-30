import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Option } from './interface/option.interface';
import { debounceTime, distinctUntilChanged, Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-search-input',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './search-input.component.html',
  styleUrl: './search-input.component.css',
})
export class SearchInputComponent implements OnInit, OnDestroy {
  @Input() options: Option[] = [];
  @Input() placeholder: string = 'Search...';
  @Input() selectedValue: any = null;
  @Input() allowAddNew: boolean = false;
  @Input() addNewText: string = 'Add new option';

  @Output() selectionChange = new EventEmitter<Option | null>();
  @Output() searchChange = new EventEmitter<string>();

  searchControl = new FormControl('');

  filteredOptions: Option[] = [];
  showDropdown = false;
  showNewInput = false;

  private destroy$ = new Subject<void>();

  ngOnInit() {
    this.filteredOptions = [...this.options];

    if (this.selectedValue) {
      const selectedOption = this.options.find((opt) => opt.value === this.selectedValue);

      if (selectedOption) {
        this.searchControl.setValue(selectedOption.label, { emitEvent: false });
      }
    }

    this.searchControl.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe((searchTerm) => {
        this.filterOptions(searchTerm || '');
        this.searchChange.emit(searchTerm || '');
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private filterOptions(searchTerm: string) {
    if (!searchTerm.trim()) {
      this.filteredOptions = [...this.options];
      return;
    }

    const term = searchTerm.toLowerCase().trim();
    this.filteredOptions = this.options.filter((option) => option.label.toLowerCase().includes(term));
  }

  onFocus() {
    this.showDropdown = true;
    this.filterOptions(this.searchControl.value || '');
  }

  onBlur() {
    setTimeout(() => {
      this.showDropdown = false;
    }, 200);
  }

  selectOption(option: Option) {
    this.searchControl.setValue(option.label, { emitEvent: false });
    this.showDropdown = false;
    this.showNewInput = false;
    this.selectionChange.emit(option);
  }

  selectAddNew() {
    this.searchControl.setValue(this.addNewText, { emitEvent: false });
    this.showDropdown = false;
    this.selectionChange.emit(null);
  }

  isSelected(option: Option): boolean {
    return this.selectedValue === option.value;
  }

  trackByFn(index: number, option: Option): any {
    return option.id || option.value;
  }

  reset() {
    this.searchControl.setValue('', { emitEvent: false });

    this.filteredOptions = [...this.options];
    this.showNewInput = false;

    this.selectionChange.emit(null);
  }
}
