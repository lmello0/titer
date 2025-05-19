import { Actor } from './actor';
import { Comment } from './comment';
import { Director } from './director';
import { Genre } from './genre';
import { Release } from './release';
import { Social } from './social';
import { Theatre } from './theatre';
import { Worker } from './worker';

export interface PlayDetails {
  playId: string;
  title: string;
  director: Director;
  synopsis: string;
  releaseYear: number;
  rating: number;
  watchCount: number;
  reviewCount: number;
  posterImageUrl: string;
  directors: Director[];
  mainActors: Actor[];
  genres: Genre[];
  duration: number;
  premiereDate: Date;
  premiereTheatre: Theatre;
  cast: Actor[];
  crew: Worker[];
  releases: Release[];
  socials: Social[];
  comments: Comment[];
}
