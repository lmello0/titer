import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormArray, FormBuilder, FormControl, ReactiveFormsModule } from '@angular/forms';
import { Option } from '../search-input/interface/option.interface';

@Component({
  selector: 'app-selectable-list',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './selectable-list.component.html',
  styleUrl: './selectable-list.component.css',
})
export class SelectableListComponent {
  @Input() options: Option[] = [];
  @Input() allowAddNew: boolean = true;
  @Input() addNewLabel: string = 'Add new option';
  @Input() placeholder: string = 'Search';

  @Output() selectedChange = new EventEmitter<Option[]>();

  selected: Option[] = [];
  showDropdown = false;
  formControl = new FormControl('');

  constructor() {}

  onFocus() {
    this.showDropdown = true;
  }

  onBlur() {
    setTimeout(() => {
      this.showDropdown = false;
    }, 200);
  }

  selectOption(option: Option): void {
    this.formControl.setValue(option.label);
    this.showDropdown = false;
  }

  onAddItem() {
    const item = this.options.find((o) => o.label === this.formControl.value);

    if (item) {
      this.selected.push(item);

      this.options.splice(
        this.options.findIndex((o) => o.id === item.id),
        1,
      );

      this.formControl.reset();
    }

    this.selectedChange.emit(this.selected);
  }

  onRemoveItem(item: Option) {
    const itemIndex = this.selected.findIndex((i) => i === item);

    if (itemIndex > -1) {
      this.selected.splice(itemIndex, 1);
      this.options.push(item);
      this.options.sort((a, b) => parseInt(a.id.toString()) - parseInt(b.id.toString()));
    }

    this.selectedChange.emit(this.selected);
  }
}
