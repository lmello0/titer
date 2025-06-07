import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Option } from '../search-input/interface/option.interface';
import { debounce, debounceTime, distinctUntilChanged, startWith, Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-selectable-list',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './selectable-list.component.html',
  styleUrl: './selectable-list.component.css',
})
export class SelectableListComponent implements OnInit, OnChanges, OnDestroy {
  @Input() options: Option[] = [];
  @Input() allowAddNew: boolean = true;
  @Input() addNewLabel: string = 'Add new option';
  @Input() placeholder: string = 'Search';
  @Input() newLabel = 'As:';
  @Input() newPlaceholder = '';

  @Output() selectedChange = new EventEmitter<Option[]>();

  selected: Option[] = [];
  filteredOptions: Option[] = [];

  showDropdown = false;
  showNewField = false;

  searchControl = new FormControl('');
  newOptionControl = new FormControl('');

  private destroy$ = new Subject<void>();

  ngOnInit(): void {
    this.filteredOptions = [...this.options];

    this.searchControl.valueChanges
      .pipe(startWith(''), debounceTime(300), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe((searchTerm) => this.filterOptions(searchTerm ?? ''));
  }

  ngOnChanges() {
    // this.filterOptions(this.searchControl.value ?? '');
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onFocus(): void {
    this.showDropdown = true;
    this.filterOptions(this.searchControl.value ?? '');
  }

  onBlur(): void {
    setTimeout(() => (this.showDropdown = false), 200);
  }

  selectOption(option?: Option): void {
    const isAddNew = !option;

    this.searchControl.setValue(isAddNew ? this.addNewLabel : option!.label, { emitEvent: false });
    this.showNewField = isAddNew;
    this.showDropdown = false;
  }

  onAddItem() {
    const label = this.searchControl.value ?? '';
    const isNew = this.searchControl.value === this.addNewLabel;

    const newItem = isNew ? this.createNewItem() : this.extractOption(label);

    if (!newItem || this.isAlreadySelected(newItem)) return;

    this.selected.push(newItem);
    this.resetInputs();
    this.emitSelected();
  }

  onRemoveItem(item: Option) {
    this.selected = this.selected.filter((i) => i !== item);

    if (item.id !== -1) {
      this.options = [...this.options, item].sort((a, b) => Number(a.id) - Number(b.id));
    }

    this.filteredOptions = [...this.options];
    this.emitSelected();
  }

  private createNewItem(): Option {
    return { id: -1, label: this.newOptionControl.value ?? '', value: -1 };
  }

  private extractOption(label: string): Option | undefined {
    const match = this.options.find((o) => o.label === label);

    if (!match) return undefined;

    this.options = this.options.filter((o) => o !== match);
    return match;
  }

  private isAlreadySelected(option: Option): boolean {
    return this.selected.some((sel) => sel.label === option.label);
  }

  private resetInputs(): void {
    this.searchControl.reset();
    this.newOptionControl.reset();
    this.showNewField = false;
  }

  private emitSelected(): void {
    this.selectedChange.emit([...this.selected]);
  }

  private filterOptions(searchTerm: string) {
    if (!searchTerm.trim()) {
      this.filteredOptions = [...this.options];
      return;
    }

    const lower = searchTerm.toLowerCase();
    this.filteredOptions = this.options.filter((o) => o.label.toLowerCase().startsWith(lower));
  }
}
