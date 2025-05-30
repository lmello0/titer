import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SearchInputComponent } from '../shared/components/search-input/search-input.component';
import { Option } from '../shared/components/search-input/interface/option.interface';

@Component({
  selector: 'app-submit-new-play',
  imports: [CommonModule, ReactiveFormsModule, SearchInputComponent],
  templateUrl: './submit-new-play.component.html',
  styleUrl: './submit-new-play.component.css',
})
export class SubmitNewPlayComponent implements OnInit {
  private formBuilder = inject(FormBuilder);

  newPlayForm!: FormGroup;

  directors: Option[] = [
    { id: '1', label: 'Christopher Nolan', value: '1' },
    { id: '2', label: 'Quentin Tarantino', value: '2' },
    { id: '3', label: 'Greta Gerwig', value: '3' },
    { id: '4', label: 'Martin Scorsese', value: '4' },
    { id: '5', label: 'Chloé Zhao', value: '5' },
  ];

  genres: Option[] = [
    { id: '1', label: 'Comedy', value: '1' },
    { id: '2', label: 'Drama', value: '2' },
    { id: '3', label: 'Action', value: '2' },
  ];

  actors: Option[] = [
    { id: '1', label: 'Actor 1', value: '1' },
    { id: '2', label: 'Actor 2', value: '2' },
    { id: '3', label: 'Actor 3', value: '3' },
    { id: '4', label: 'Actor 4', value: '4' },
    { id: '5', label: 'Actor 5', value: '5' },
  ];

  previewUrl: string | ArrayBuffer | null = null;

  newDirectorName: string = '';
  showNewDirectorInput: boolean = false;

  ngOnInit(): void {
    this.newPlayForm = this.formBuilder.group({
      playPosterUrl: [''],
      title: ['', [Validators.required, Validators.maxLength(30)]],
      synopsis: ['', Validators.required],
      director: ['', [Validators.required]],
    });
  }

  onDirectorSelect(option: Option | null) {
    if (!option) {
      this.showNewDirectorInput = true;
      return;
    }

    this.showNewDirectorInput = false;
    this.newPlayForm.patchValue({
      director: option?.value || '',
    });
  }

  onGenreSelect(option: Option | null) {
    this.newPlayForm.patchValue({
      genre: option?.value || '',
    });
  }

  onActorSelect(option: Option | null) {
    this.newPlayForm.patchValue({
      actor: option?.value || '',
    });
  }

  onNewDirectorvalue(newName: string) {
    this.newDirectorName = newName;

    if (!newName.trim()) {
      this.newPlayForm.patchValue({ director: null });
      return;
    }

    this.newPlayForm.patchValue({ director: { directorId: -1, directorName: newName.trim() } });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files[0]) {
      const file = input.files[0];
      const reader = new FileReader();

      reader.onload = () => {
        this.previewUrl = reader.result;
      };

      reader.readAsDataURL(file);
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();

    if (event.dataTransfer?.files && event.dataTransfer.files[0]) {
      this.readFile(event.dataTransfer.files[0]);
    }
  }

  removeImage(event: MouseEvent): void {
    event.stopPropagation();
    console.log(this.previewUrl);
    this.previewUrl = null;
  }

  private readFile(file: File) {
    const reader = new FileReader();

    reader.onload = () => {
      this.previewUrl = reader.result;
    };

    reader.readAsDataURL(file);
  }
}
