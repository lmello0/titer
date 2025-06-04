import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { SearchInputWithFieldComponent } from '../../shared/components/search-input-with-field/search-input-with-field.component';
import { Option } from '../../shared/components/search-input/interface/option.interface';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, Subject, takeUntil } from 'rxjs';
import { Actor } from '../../shared/interfaces/actor.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-character-input',
  imports: [CommonModule, ReactiveFormsModule, SearchInputWithFieldComponent],
  templateUrl: './character-input.component.html',
  styleUrl: './character-input.component.css',
})
export class CharacterInputComponent implements OnInit, OnDestroy {
  @Input() actorOptions!: Option[];

  characterControl: FormControl = new FormControl('');

  @Output() characterChange: EventEmitter<Actor> = new EventEmitter<Actor>();

  private destroy$: Subject<void> = new Subject<void>();

  actorId: string = '';
  actorName: string = '';

  ngOnInit(): void {
    this.characterControl.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe((characterValue) =>
        this.characterChange.emit({ actorId: this.actorId, actorName: this.actorName, character: characterValue }),
      );
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onCastSelect(option: Option | null) {
    this.actorId = option?.value || '-1';
    this.actorName = option?.value || '';
  }

  onFieldChange(value: string | null) {
    this.actorId = '-1';
    this.actorName = value || '';
  }
}
