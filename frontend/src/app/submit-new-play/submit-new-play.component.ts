import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { DirectorService } from '../services/director/director.service';
import { GenreService } from '../services/genre/genre.service';
import { Director } from '../shared/interfaces/director.interface';
import { Genre } from '../shared/interfaces/genre.interface';
import { Option } from '../shared/components/search-input/interface/option.interface';
import { SearchInputWithFieldComponent } from '../shared/components/search-input-with-field/search-input-with-field.component';
import { SelectableListComponent } from '../shared/components/selectable-list/selectable-list.component';

@Component({
  selector: 'app-submit-new-play',
  imports: [CommonModule, ReactiveFormsModule, RouterLink, SearchInputWithFieldComponent, SelectableListComponent],
  templateUrl: './submit-new-play.component.html',
  styleUrl: './submit-new-play.component.css',
})
export class SubmitNewPlayComponent implements OnInit {
  playForm!: FormGroup;

  previewUrl: string | ArrayBuffer | null = null;

  directors: Director[] = [];
  directorsOptions: Option[] = [];

  backendGenres: Genre[] = [];
  genreOptions: Option[] = [];
  currentGenreSelection = new FormControl();

  constructor(
    private fb: FormBuilder,
    private directorService: DirectorService,
    private genreService: GenreService,
  ) {}

  ngOnInit(): void {
    this.playForm = this.fb.group({
      playPosterUrl: [''],
      title: ['', [Validators.required, Validators.maxLength(120)]],
      playwright: ['', [Validators.required, Validators.maxLength(120)]],
      synopsis: ['', [Validators.maxLength(1000)]],
      originalLanguage: ['', [Validators.maxLength(64)]],
      productionCompany: ['', [Validators.maxLength(120)]],
      premiereDate: [''],
      duration: [120, [Validators.min(1), Validators.max(400)]],
      director: this.fb.group({
        directorId: ['-1'],
        directorName: ['', Validators.required],
      }),
      premiereTheatre: this.fb.group({ theatreId: '-1', theatreName: ['', Validators.required] }),
      genres: this.fb.array([]),
      cast: this.fb.array([]),
      crew: this.fb.array([]),
      releases: this.fb.array([]),
      socials: this.fb.array([]),
      status: ['unverified'],
    });

    this.directorService.getAllDirectors().subscribe((directors) => {
      this.directors = directors;
      this.directorsOptions = this.directors.map((director) => ({
        id: director.directorId,
        label: director.directorName,
        value: director.directorId,
      }));
    });

    this.genreService.getAllGenres().subscribe((genres) => {
      this.backendGenres = genres;
      this.genreOptions = this.backendGenres.map((genre) => ({
        id: genre.genreId,
        label: genre.genreName,
        value: genre.genreId,
      }));
    });
  }

  get genres(): FormArray {
    return this.playForm.get('genres') as FormArray;
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files[0]) {
      this.readFile(input.files[0]);
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
    this.previewUrl = null;
  }

  onFieldChange(value: string | null): void {
    this.playForm.get('director.directorName')?.setValue(value ?? '');
  }

  onDirectorSelect(option: Option | null): void {
    const directorControl = this.playForm.get('director');

    if (option) {
      directorControl?.patchValue({ directorId: String(option.value), directorName: option.label });
      return;
    }

    directorControl?.reset({
      directorId: '-1',
      directorName: '',
    });
  }

  onUpdateGenres(newGenres: Option[]): void {
    this.genres.clear();

    newGenres.forEach((genre) => {
      this.genres.push(
        this.fb.group({
          genreId: [genre.value],
          genreName: [genre.label],
        }),
      );
    });
  }

  onSubmit(): void {
    this.playForm.markAllAsTouched();

    if (this.playForm.invalid) {
      return;
    }
  }

  private readFile(file: File): void {
    const reader = new FileReader();

    reader.onload = () => {
      this.previewUrl = reader.result;
    };

    reader.readAsDataURL(file);
  }
}
