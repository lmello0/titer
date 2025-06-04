import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { DirectorService } from '../services/director/director.service';
import { GenreService } from '../services/genre/genre.service';
import { ActorService } from '../services/actor/actor.service';
import { Director } from '../shared/interfaces/director.interface';
import { Genre } from '../shared/interfaces/genre.interface';
import { Actor } from '../shared/interfaces/actor.interface';
import { Option } from '../shared/components/search-input/interface/option.interface';
import { SearchInputWithFieldComponent } from '../shared/components/search-input-with-field/search-input-with-field.component';
import { SelectableListComponent } from '../shared/components/selectable-list/selectable-list.component';

@Component({
  selector: 'app-submit-new-play',
  imports: [CommonModule, ReactiveFormsModule, SearchInputWithFieldComponent, SelectableListComponent],
  templateUrl: './submit-new-play.component.html',
  styleUrl: './submit-new-play.component.css',
})
export class SubmitNewPlayComponent implements OnInit {
  playForm!: FormGroup;

  previewUrl: string | ArrayBuffer | null = null;

  directors!: Director[];
  directorsOptions: Option[] = [];

  backendGenres!: Genre[];
  genreOptions: Option[] = [];
  currentGenreSelection = new FormControl();

  actors!: Actor[];
  actorOptions: Option[] = [];

  constructor(
    private fb: FormBuilder,
    private directorService: DirectorService,
    private genreService: GenreService,
    private actorService: ActorService,
  ) {}

  ngOnInit(): void {
    this.playForm = this.fb.group({
      playPosterUrl: [''],
      title: ['', [Validators.required, Validators.maxLength(30)]],
      director: this.fb.group({
        directorId: -1,
        directorName: ['', Validators.required],
      }),
      synopsis: ['', Validators.required],
      duration: '',
      premiereDate: '',
      premiereTheatre: this.fb.group({ theatreId: -1, theatreName: ['', Validators.required] }),
      genres: this.fb.array([]),
      cast: this.fb.array([]),
      crew: this.fb.array([]),
      releases: this.fb.array([]),
      socials: this.fb.array([]),
    });

    this.directorService.getAllDirectors().subscribe((directors) => {
      this.directors = directors;
      this.directorsOptions = this.directors.map((d) => {
        return { id: d.directorId, label: d.directorName, value: d.directorId };
      });
    });

    this.genreService.getAllGenres().subscribe((genres) => {
      this.backendGenres = genres;
      this.genreOptions = this.backendGenres.map((g) => {
        return { id: g.genreId, label: g.genreName, value: g.genreId };
      });
    });

    this.actorService.getAllActors().subscribe((actors) => {
      this.actors = actors;
      this.actorOptions = this.actors.map((a) => {
        return { id: a.actorId, label: a.actorName, value: a.actorId };
      });
    });
  }

  get genres() {
    return this.playForm.get('genres') as FormArray;
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

  onFieldChange(value: string | null) {
    console.log(this.playForm.value);
    console.log(value);
  }

  onDirectorSelect(option: Option | null) {
    const directorControl = this.playForm.get('director');

    if (option) {
      directorControl?.patchValue({ directorId: option.value, directorName: option.label });
      return;
    }

    directorControl?.reset({
      directorId: -1,
      directorName: '',
    });
  }

  onGenreSelect(option: Option | null) {
    const genreControl = this.playForm.get('genres') as FormArray;

    if (!option) return;

    genreControl.push(this.fb.group({ genreId: option.value, genreName: option.label }));
  }

  onCastSelect(option: Option | null) {
    const actorControl = this.playForm.get('cast') as FormArray;

    if (!option) return;

    actorControl.push(this.fb.group({ actorId: option.id, actorName: null }));
  }

  onUpdateGenres(newGenres: Option[]) {
    this.genres.clear();

    newGenres.forEach((ng) => {
      this.genres.push(
        this.fb.group({
          genreId: [ng.value],
          genreName: [ng.label],
        }),
      );
    });
  }
}
