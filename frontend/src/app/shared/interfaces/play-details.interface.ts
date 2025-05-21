import { Actor } from './actor.interface';
import { Review } from './review.interface';
import { Director } from './director.interface';
import { Genre } from './genre.interface';
import { Release } from './release.interface';
import { Social } from './social.interface';
import { Theatre } from './theatre.interface';
import { Worker } from './worker.interface';

export interface PlayDetails {
  playId: number;
  title: string;
  director: Director;
  synopsis: string;
  releaseYear: number;
  rating: number;
  watchCount: number;
  reviewCount: number;
  posterImageUrl: string;
  mainActors: Actor[];
  genres: Genre[];
  duration: number;
  premiereDate: Date;
  premiereTheatre: Theatre;
  cast: Actor[];
  crew: Worker[];
  releases: Release[];
  socials: Social[];
  comments: Review[];
}
