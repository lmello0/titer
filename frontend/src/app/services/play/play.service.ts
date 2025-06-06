import { Injectable } from '@angular/core';
import { PlayDetails } from '../../shared/interfaces/play-details.interface';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PlayService {
  private mockPlays: PlayDetails[] = [
    {
      playId: 1,
      title: 'The Shawshank Redemption',
      director: { directorId: '1', directorName: 'Frank Darabont' },
      synopsis:
        'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Perferendis, necessitatibus nulla minus aliquid incidunt consectetur ipsam pariatur quidem officiis cupiditate maxime placeat vel omnis aliquam! Eveniet adipisci sed veritatis tenetur.',
      releaseYear: 1994,
      rating: 1 + Math.round(Math.random() * 5 * 10) / 10,
      watchCount: Math.floor(Math.random() * (1_000_000 * 10)),
      reviewCount: Math.floor(Math.random() * (1_000_000 * 10)),
      posterImageUrl:
        'https://image.tmdb.org/t/p/w600_and_h900_bestv2/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg',
      mainActors: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
      ],
      genres: [
        { genreId: '1', genreName: 'Genre 1' },
        { genreId: '2', genreName: 'Genre 2' },
        { genreId: '3', genreName: 'Genre 3' },
      ],
      duration: 30 + Math.floor(Math.random() * 120),
      premiereDate: new Date(),
      premiereTheatre: { theatreId: '1', theatreName: 'Theatre' },
      cast: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
        { actorId: '5', actorName: 'Actor 5', character: 'Character 5' },
        { actorId: '6', actorName: 'Actor 6', character: 'Character 6' },
        { actorId: '7', actorName: 'Actor 7', character: 'Character 7' },
        { actorId: '8', actorName: 'Actor 8', character: 'Character 8' },
        { actorId: '9', actorName: 'Actor 9', character: 'Character 9' },
        { actorId: '10', actorName: 'Actor 10', character: 'Character 10' },
      ],
      crew: [
        { workerId: '1', workerName: 'Crew 1', role: 'Role 1' },
        { workerId: '2', workerName: 'Crew 2', role: 'Role 2' },
        { workerId: '3', workerName: 'Crew 3', role: 'Role 3' },
        { workerId: '4', workerName: 'Crew 4', role: 'Role 4' },
        { workerId: '5', workerName: 'Crew 5', role: 'Role 5' },
        { workerId: '6', workerName: 'Crew 6', role: 'Role 6' },
        { workerId: '7', workerName: 'Crew 7', role: 'Role 7' },
        { workerId: '8', workerName: 'Crew 8', role: 'Role 8' },
        { workerId: '9', workerName: 'Crew 9', role: 'Role 9' },
        { workerId: '10', workerName: 'Crew 10', role: 'Role 10' },
      ],
      releases: [
        {
          releaseDate: new Date('2025-05-13'),
          theatres: [
            { theatreId: '1', theatreName: 'Theatre 1' },
            { theatreId: '2', theatreName: 'Theatre 2' },
          ],
        },
        {
          releaseDate: new Date('2025-05-12'),
          theatres: [
            { theatreId: '3', theatreName: 'Theatre 3' },
            { theatreId: '4', theatreName: 'Theatre 4' },
            { theatreId: '5', theatreName: 'Theatre 5' },
          ],
        },
        {
          releaseDate: new Date('2025-05-11'),
          theatres: [{ theatreId: '6', theatreName: 'Theatre 6' }],
        },
      ],
      socials: [
        {
          platform: 'instagram',
          socialIconUrl:
            'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/1024px-Instagram_icon.png',
          socialLink: 'https://url.com',
          socialName: 'frank_darabont',
        },
      ],
      comments: [
        {
          id: 1,
          author: {
            userId: 1,
            username: 'Jane Doe',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
          },
          date: new Date(),
          content: 'This is such a helpful post! Thanks for sharing',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 1,
        },
        {
          id: 2,
          author: {
            userId: 2,
            username: 'John Smith',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=3',
          },
          date: new Date(),
          content: 'I disagree with your take, but interesting perspective!',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 1,
        },
      ],
    },
    {
      playId: 2,
      title: 'The Godfather',
      director: { directorId: '1', directorName: 'Francis Ford Coppola' },
      synopsis:
        'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Perferendis, necessitatibus nulla minus aliquid incidunt consectetur ipsam pariatur quidem officiis cupiditate maxime placeat vel omnis aliquam! Eveniet adipisci sed veritatis tenetur.',
      releaseYear: 1972,
      rating: 1 + Math.round(Math.random() * 5 * 10) / 10,
      watchCount: Math.floor(Math.random() * (1_000_000 * 10)),
      reviewCount: Math.floor(Math.random() * (1_000_000 * 10)),
      posterImageUrl:
        'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/3bhkrj58Vtu7enYsRolD1fZdja1.jpg',
      mainActors: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
      ],
      genres: [
        { genreId: '1', genreName: 'Genre 1' },
        { genreId: '2', genreName: 'Genre 2' },
        { genreId: '3', genreName: 'Genre 3' },
      ],
      duration: 30 + Math.floor(Math.random() * 120),
      premiereDate: new Date(),
      premiereTheatre: { theatreId: '1', theatreName: 'Theatre' },
      cast: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
        { actorId: '5', actorName: 'Actor 5', character: 'Character 5' },
        { actorId: '6', actorName: 'Actor 6', character: 'Character 6' },
        { actorId: '7', actorName: 'Actor 7', character: 'Character 7' },
        { actorId: '8', actorName: 'Actor 8', character: 'Character 8' },
        { actorId: '9', actorName: 'Actor 9', character: 'Character 9' },
        { actorId: '10', actorName: 'Actor 10', character: 'Character 10' },
      ],
      crew: [
        { workerId: '1', workerName: 'Crew 1', role: 'Role 1' },
        { workerId: '2', workerName: 'Crew 2', role: 'Role 2' },
        { workerId: '3', workerName: 'Crew 3', role: 'Role 3' },
        { workerId: '4', workerName: 'Crew 4', role: 'Role 4' },
        { workerId: '5', workerName: 'Crew 5', role: 'Role 5' },
        { workerId: '6', workerName: 'Crew 6', role: 'Role 6' },
        { workerId: '7', workerName: 'Crew 7', role: 'Role 7' },
        { workerId: '8', workerName: 'Crew 8', role: 'Role 8' },
        { workerId: '9', workerName: 'Crew 9', role: 'Role 9' },
        { workerId: '10', workerName: 'Crew 10', role: 'Role 10' },
      ],
      releases: [
        {
          releaseDate: new Date('2025-05-13'),
          theatres: [
            { theatreId: '1', theatreName: 'Theatre 1' },
            { theatreId: '2', theatreName: 'Theatre 2' },
          ],
        },
        {
          releaseDate: new Date('2025-05-12'),
          theatres: [
            { theatreId: '3', theatreName: 'Theatre 3' },
            { theatreId: '4', theatreName: 'Theatre 4' },
            { theatreId: '5', theatreName: 'Theatre 5' },
          ],
        },
        {
          releaseDate: new Date('2025-05-11'),
          theatres: [{ theatreId: '6', theatreName: 'Theatre 6' }],
        },
      ],
      socials: [
        {
          platform: 'instagram',
          socialIconUrl:
            'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/1024px-Instagram_icon.png',
          socialLink: 'https://url.com',
          socialName: 'ffcoppola',
        },
      ],
      comments: [
        {
          id: 1,
          author: {
            userId: 1,
            username: 'Jane Doe',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
          },
          date: new Date(),
          content: 'This is such a helpful post! Thanks for sharing',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 2,
        },
        {
          id: 2,
          author: {
            userId: 2,
            username: 'John Smith',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=3',
          },
          date: new Date(),
          content: 'I disagree with your take, but interesting perspective!',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 2,
        },
      ],
    },
    {
      playId: 3,
      title: "Schindler's List",
      director: { directorId: '1', directorName: 'Steven Spielberg' },
      synopsis:
        'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Perferendis, necessitatibus nulla minus aliquid incidunt consectetur ipsam pariatur quidem officiis cupiditate maxime placeat vel omnis aliquam! Eveniet adipisci sed veritatis tenetur.',
      releaseYear: 1993,
      rating: 1 + Math.round(Math.random() * 5 * 10) / 10,
      watchCount: Math.floor(Math.random() * (1_000_000 * 10)),
      reviewCount: Math.floor(Math.random() * (1_000_000 * 10)),
      posterImageUrl:
        'https://image.tmdb.org/t/p/w600_and_h900_bestv2/sF1U4EUQS8YHUYjNl3pMGNIQyr0.jpg',
      mainActors: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
      ],
      genres: [
        { genreId: '1', genreName: 'Genre 1' },
        { genreId: '2', genreName: 'Genre 2' },
        { genreId: '3', genreName: 'Genre 3' },
      ],
      duration: 30 + Math.floor(Math.random() * 120),
      premiereDate: new Date(),
      premiereTheatre: { theatreId: '1', theatreName: 'Theatre' },
      cast: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
        { actorId: '5', actorName: 'Actor 5', character: 'Character 5' },
        { actorId: '6', actorName: 'Actor 6', character: 'Character 6' },
        { actorId: '7', actorName: 'Actor 7', character: 'Character 7' },
        { actorId: '8', actorName: 'Actor 8', character: 'Character 8' },
        { actorId: '9', actorName: 'Actor 9', character: 'Character 9' },
        { actorId: '10', actorName: 'Actor 10', character: 'Character 10' },
      ],
      crew: [
        { workerId: '1', workerName: 'Crew 1', role: 'Role 1' },
        { workerId: '2', workerName: 'Crew 2', role: 'Role 2' },
        { workerId: '3', workerName: 'Crew 3', role: 'Role 3' },
        { workerId: '4', workerName: 'Crew 4', role: 'Role 4' },
        { workerId: '5', workerName: 'Crew 5', role: 'Role 5' },
        { workerId: '6', workerName: 'Crew 6', role: 'Role 6' },
        { workerId: '7', workerName: 'Crew 7', role: 'Role 7' },
        { workerId: '8', workerName: 'Crew 8', role: 'Role 8' },
        { workerId: '9', workerName: 'Crew 9', role: 'Role 9' },
        { workerId: '10', workerName: 'Crew 10', role: 'Role 10' },
      ],
      releases: [
        {
          releaseDate: new Date('2025-05-13'),
          theatres: [
            { theatreId: '1', theatreName: 'Theatre 1' },
            { theatreId: '2', theatreName: 'Theatre 2' },
          ],
        },
        {
          releaseDate: new Date('2025-05-12'),
          theatres: [
            { theatreId: '3', theatreName: 'Theatre 3' },
            { theatreId: '4', theatreName: 'Theatre 4' },
            { theatreId: '5', theatreName: 'Theatre 5' },
          ],
        },
        {
          releaseDate: new Date('2025-05-11'),
          theatres: [{ theatreId: '6', theatreName: 'Theatre 6' }],
        },
      ],
      socials: [
        {
          platform: 'instagram',
          socialIconUrl:
            'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/1024px-Instagram_icon.png',
          socialLink: 'https://url.com',
          socialName: 'spielberg_steven',
        },
      ],
      comments: [
        {
          id: 1,
          author: {
            userId: 1,
            username: 'Jane Doe',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
          },
          date: new Date(),
          content: 'This is such a helpful post! Thanks for sharing',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 3,
        },
        {
          id: 2,
          author: {
            userId: 2,
            username: 'John Smith',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=3',
          },
          date: new Date(),
          content: 'I disagree with your take, but interesting perspective!',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 3,
        },
      ],
    },
    {
      playId: 4,
      title: 'The Dark Knight',
      director: { directorId: '1', directorName: 'Christopher Nolan' },
      synopsis:
        'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Perferendis, necessitatibus nulla minus aliquid incidunt consectetur ipsam pariatur quidem officiis cupiditate maxime placeat vel omnis aliquam! Eveniet adipisci sed veritatis tenetur.',
      releaseYear: 2008,
      rating: 1 + Math.round(Math.random() * 5 * 10) / 10,
      watchCount: Math.floor(Math.random() * (1_000_000 * 10)),
      reviewCount: Math.floor(Math.random() * (1_000_000 * 10)),
      posterImageUrl:
        'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/qJ2tW6WMUDux911r6m7haRef0WH.jpg',
      mainActors: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
      ],
      genres: [
        { genreId: '1', genreName: 'Genre 1' },
        { genreId: '2', genreName: 'Genre 2' },
        { genreId: '3', genreName: 'Genre 3' },
      ],
      duration: 30 + Math.floor(Math.random() * 120),
      premiereDate: new Date(),
      premiereTheatre: { theatreId: '1', theatreName: 'Theatre' },
      cast: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
        { actorId: '5', actorName: 'Actor 5', character: 'Character 5' },
        { actorId: '6', actorName: 'Actor 6', character: 'Character 6' },
        { actorId: '7', actorName: 'Actor 7', character: 'Character 7' },
        { actorId: '8', actorName: 'Actor 8', character: 'Character 8' },
        { actorId: '9', actorName: 'Actor 9', character: 'Character 9' },
        { actorId: '10', actorName: 'Actor 10', character: 'Character 10' },
      ],
      crew: [
        { workerId: '1', workerName: 'Crew 1', role: 'Role 1' },
        { workerId: '2', workerName: 'Crew 2', role: 'Role 2' },
        { workerId: '3', workerName: 'Crew 3', role: 'Role 3' },
        { workerId: '4', workerName: 'Crew 4', role: 'Role 4' },
        { workerId: '5', workerName: 'Crew 5', role: 'Role 5' },
        { workerId: '6', workerName: 'Crew 6', role: 'Role 6' },
        { workerId: '7', workerName: 'Crew 7', role: 'Role 7' },
        { workerId: '8', workerName: 'Crew 8', role: 'Role 8' },
        { workerId: '9', workerName: 'Crew 9', role: 'Role 9' },
        { workerId: '10', workerName: 'Crew 10', role: 'Role 10' },
      ],
      releases: [
        {
          releaseDate: new Date('2025-05-13'),
          theatres: [
            { theatreId: '1', theatreName: 'Theatre 1' },
            { theatreId: '2', theatreName: 'Theatre 2' },
          ],
        },
        {
          releaseDate: new Date('2025-05-12'),
          theatres: [
            { theatreId: '3', theatreName: 'Theatre 3' },
            { theatreId: '4', theatreName: 'Theatre 4' },
            { theatreId: '5', theatreName: 'Theatre 5' },
          ],
        },
        {
          releaseDate: new Date('2025-05-11'),
          theatres: [{ theatreId: '6', theatreName: 'Theatre 6' }],
        },
      ],
      socials: [
        {
          platform: 'instagram',
          socialIconUrl:
            'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/1024px-Instagram_icon.png',
          socialLink: 'https://url.com',
          socialName: 'chrisnolan',
        },
      ],
      comments: [
        {
          id: 1,
          author: {
            userId: 1,
            username: 'Jane Doe',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
          },
          date: new Date(),
          content: 'This is such a helpful post! Thanks for sharing',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 4,
        },
        {
          id: 2,
          author: {
            userId: 2,
            username: 'John Smith',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=3',
          },
          date: new Date(),
          content: 'I disagree with your take, but interesting perspective!',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 4,
        },
      ],
    },
    {
      playId: 5,
      title: 'Spirited Away',
      director: { directorId: '1', directorName: 'Hayao Miyazaki' },
      synopsis:
        'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Perferendis, necessitatibus nulla minus aliquid incidunt consectetur ipsam pariatur quidem officiis cupiditate maxime placeat vel omnis aliquam! Eveniet adipisci sed veritatis tenetur.',
      releaseYear: 2001,
      rating: 1 + Math.round(Math.random() * 5 * 10) / 10,
      watchCount: Math.floor(Math.random() * (1_000_000 * 10)),
      reviewCount: Math.floor(Math.random() * (1_000_000 * 10)),
      posterImageUrl:
        'https://image.tmdb.org/t/p/w600_and_h900_bestv2/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg',
      mainActors: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
      ],
      genres: [
        { genreId: '1', genreName: 'Genre 1' },
        { genreId: '2', genreName: 'Genre 2' },
        { genreId: '3', genreName: 'Genre 3' },
      ],
      duration: 30 + Math.floor(Math.random() * 120),
      premiereDate: new Date(),
      premiereTheatre: { theatreId: '1', theatreName: 'Theatre' },
      cast: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
        { actorId: '5', actorName: 'Actor 5', character: 'Character 5' },
        { actorId: '6', actorName: 'Actor 6', character: 'Character 6' },
        { actorId: '7', actorName: 'Actor 7', character: 'Character 7' },
        { actorId: '8', actorName: 'Actor 8', character: 'Character 8' },
        { actorId: '9', actorName: 'Actor 9', character: 'Character 9' },
        { actorId: '10', actorName: 'Actor 10', character: 'Character 10' },
      ],
      crew: [
        { workerId: '1', workerName: 'Crew 1', role: 'Role 1' },
        { workerId: '2', workerName: 'Crew 2', role: 'Role 2' },
        { workerId: '3', workerName: 'Crew 3', role: 'Role 3' },
        { workerId: '4', workerName: 'Crew 4', role: 'Role 4' },
        { workerId: '5', workerName: 'Crew 5', role: 'Role 5' },
        { workerId: '6', workerName: 'Crew 6', role: 'Role 6' },
        { workerId: '7', workerName: 'Crew 7', role: 'Role 7' },
        { workerId: '8', workerName: 'Crew 8', role: 'Role 8' },
        { workerId: '9', workerName: 'Crew 9', role: 'Role 9' },
        { workerId: '10', workerName: 'Crew 10', role: 'Role 10' },
      ],
      releases: [
        {
          releaseDate: new Date('2025-05-13'),
          theatres: [
            { theatreId: '1', theatreName: 'Theatre 1' },
            { theatreId: '2', theatreName: 'Theatre 2' },
          ],
        },
        {
          releaseDate: new Date('2025-05-12'),
          theatres: [
            { theatreId: '3', theatreName: 'Theatre 3' },
            { theatreId: '4', theatreName: 'Theatre 4' },
            { theatreId: '5', theatreName: 'Theatre 5' },
          ],
        },
        {
          releaseDate: new Date('2025-05-11'),
          theatres: [{ theatreId: '6', theatreName: 'Theatre 6' }],
        },
      ],
      socials: [
        {
          platform: 'instagram',
          socialIconUrl:
            'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/1024px-Instagram_icon.png',
          socialLink: 'https://url.com',
          socialName: 'miyazaki_hayao',
        },
      ],
      comments: [
        {
          id: 1,
          author: {
            userId: 1,
            username: 'Jane Doe',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
          },
          date: new Date(),
          content: 'This is such a helpful post! Thanks for sharing',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 5,
        },
        {
          id: 2,
          author: {
            userId: 2,
            username: 'John Smith',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=3',
          },
          date: new Date(),
          content: 'I disagree with your take, but interesting perspective!',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 5,
        },
      ],
    },
    {
      playId: 6,
      title: 'Parasite',
      director: { directorId: '1', directorName: 'Bong Joon-Ho' },
      synopsis:
        'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Perferendis, necessitatibus nulla minus aliquid incidunt consectetur ipsam pariatur quidem officiis cupiditate maxime placeat vel omnis aliquam! Eveniet adipisci sed veritatis tenetur.',
      releaseYear: 2019,
      rating: 1 + Math.round(Math.random() * 5 * 10) / 10,
      watchCount: Math.floor(Math.random() * (1_000_000 * 10)),
      reviewCount: Math.floor(Math.random() * (1_000_000 * 10)),
      posterImageUrl:
        'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg',
      mainActors: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
      ],
      genres: [
        { genreId: '1', genreName: 'Genre 1' },
        { genreId: '2', genreName: 'Genre 2' },
        { genreId: '3', genreName: 'Genre 3' },
      ],
      duration: 30 + Math.floor(Math.random() * 120),
      premiereDate: new Date(),
      premiereTheatre: { theatreId: '1', theatreName: 'Theatre' },
      cast: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
        { actorId: '5', actorName: 'Actor 5', character: 'Character 5' },
        { actorId: '6', actorName: 'Actor 6', character: 'Character 6' },
        { actorId: '7', actorName: 'Actor 7', character: 'Character 7' },
        { actorId: '8', actorName: 'Actor 8', character: 'Character 8' },
        { actorId: '9', actorName: 'Actor 9', character: 'Character 9' },
        { actorId: '10', actorName: 'Actor 10', character: 'Character 10' },
      ],
      crew: [
        { workerId: '1', workerName: 'Crew 1', role: 'Role 1' },
        { workerId: '2', workerName: 'Crew 2', role: 'Role 2' },
        { workerId: '3', workerName: 'Crew 3', role: 'Role 3' },
        { workerId: '4', workerName: 'Crew 4', role: 'Role 4' },
        { workerId: '5', workerName: 'Crew 5', role: 'Role 5' },
        { workerId: '6', workerName: 'Crew 6', role: 'Role 6' },
        { workerId: '7', workerName: 'Crew 7', role: 'Role 7' },
        { workerId: '8', workerName: 'Crew 8', role: 'Role 8' },
        { workerId: '9', workerName: 'Crew 9', role: 'Role 9' },
        { workerId: '10', workerName: 'Crew 10', role: 'Role 10' },
      ],
      releases: [
        {
          releaseDate: new Date('2025-05-13'),
          theatres: [
            { theatreId: '1', theatreName: 'Theatre 1' },
            { theatreId: '2', theatreName: 'Theatre 2' },
          ],
        },
        {
          releaseDate: new Date('2025-05-12'),
          theatres: [
            { theatreId: '3', theatreName: 'Theatre 3' },
            { theatreId: '4', theatreName: 'Theatre 4' },
            { theatreId: '5', theatreName: 'Theatre 5' },
          ],
        },
        {
          releaseDate: new Date('2025-05-11'),
          theatres: [{ theatreId: '6', theatreName: 'Theatre 6' }],
        },
      ],
      socials: [
        {
          platform: 'instagram',
          socialIconUrl:
            'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/1024px-Instagram_icon.png',
          socialLink: 'https://url.com',
          socialName: 'bong_joon_ho',
        },
      ],
      comments: [
        {
          id: 1,
          author: {
            userId: 1,
            username: 'Jane Doe',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
          },
          date: new Date(),
          content: 'This is such a helpful post! Thanks for sharing',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 6,
        },
        {
          id: 2,
          author: {
            userId: 2,
            username: 'John Smith',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=3',
          },
          date: new Date(),
          content: 'I disagree with your take, but interesting perspective!',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 6,
        },
      ],
    },
    {
      playId: 7,
      title: 'The Green Mile',
      director: { directorId: '1', directorName: 'Frank Darabont' },
      synopsis:
        'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Perferendis, necessitatibus nulla minus aliquid incidunt consectetur ipsam pariatur quidem officiis cupiditate maxime placeat vel omnis aliquam! Eveniet adipisci sed veritatis tenetur.',
      releaseYear: 2000,
      rating: 1 + Math.round(Math.random() * 5 * 10) / 10,
      watchCount: Math.floor(Math.random() * (1_000_000 * 10)),
      reviewCount: Math.floor(Math.random() * (1_000_000 * 10)),
      posterImageUrl:
        'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/8VG8fDNiy50H4FedGwdSVUPoaJe.jpg',
      mainActors: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
      ],
      genres: [
        { genreId: '1', genreName: 'Genre 1' },
        { genreId: '2', genreName: 'Genre 2' },
        { genreId: '3', genreName: 'Genre 3' },
      ],
      duration: 30 + Math.floor(Math.random() * 120),
      premiereDate: new Date(),
      premiereTheatre: { theatreId: '1', theatreName: 'Theatre' },
      cast: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
        { actorId: '5', actorName: 'Actor 5', character: 'Character 5' },
        { actorId: '6', actorName: 'Actor 6', character: 'Character 6' },
        { actorId: '7', actorName: 'Actor 7', character: 'Character 7' },
        { actorId: '8', actorName: 'Actor 8', character: 'Character 8' },
        { actorId: '9', actorName: 'Actor 9', character: 'Character 9' },
        { actorId: '10', actorName: 'Actor 10', character: 'Character 10' },
      ],
      crew: [
        { workerId: '1', workerName: 'Crew 1', role: 'Role 1' },
        { workerId: '2', workerName: 'Crew 2', role: 'Role 2' },
        { workerId: '3', workerName: 'Crew 3', role: 'Role 3' },
        { workerId: '4', workerName: 'Crew 4', role: 'Role 4' },
        { workerId: '5', workerName: 'Crew 5', role: 'Role 5' },
        { workerId: '6', workerName: 'Crew 6', role: 'Role 6' },
        { workerId: '7', workerName: 'Crew 7', role: 'Role 7' },
        { workerId: '8', workerName: 'Crew 8', role: 'Role 8' },
        { workerId: '9', workerName: 'Crew 9', role: 'Role 9' },
        { workerId: '10', workerName: 'Crew 10', role: 'Role 10' },
      ],
      releases: [
        {
          releaseDate: new Date('2025-05-13'),
          theatres: [
            { theatreId: '1', theatreName: 'Theatre 1' },
            { theatreId: '2', theatreName: 'Theatre 2' },
          ],
        },
        {
          releaseDate: new Date('2025-05-12'),
          theatres: [
            { theatreId: '3', theatreName: 'Theatre 3' },
            { theatreId: '4', theatreName: 'Theatre 4' },
            { theatreId: '5', theatreName: 'Theatre 5' },
          ],
        },
        {
          releaseDate: new Date('2025-05-11'),
          theatres: [{ theatreId: '6', theatreName: 'Theatre 6' }],
        },
      ],
      socials: [
        {
          platform: 'instagram',
          socialIconUrl:
            'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/1024px-Instagram_icon.png',
          socialLink: 'https://url.com',
          socialName: 'frank_darabont',
        },
      ],
      comments: [
        {
          id: 1,
          author: {
            userId: 1,
            username: 'Jane Doe',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
          },
          date: new Date(),
          content: 'This is such a helpful post! Thanks for sharing',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 7,
        },
        {
          id: 2,
          author: {
            userId: 2,
            username: 'John Smith',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=3',
          },
          date: new Date(),
          content: 'I disagree with your take, but interesting perspective!',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 7,
        },
      ],
    },
    {
      playId: 8,
      title: 'Pulp Fiction',
      director: { directorId: '1', directorName: 'Quentin Tarantino' },
      synopsis:
        'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Perferendis, necessitatibus nulla minus aliquid incidunt consectetur ipsam pariatur quidem officiis cupiditate maxime placeat vel omnis aliquam! Eveniet adipisci sed veritatis tenetur.',
      releaseYear: 1995,
      rating: 1 + Math.round(Math.random() * 5 * 10) / 10,
      watchCount: Math.floor(Math.random() * (1_000_000 * 10)),
      reviewCount: Math.floor(Math.random() * (1_000_000 * 10)),
      posterImageUrl:
        'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/vQWk5YBFWF4bZaofAbv0tShwBvQ.jpg',
      mainActors: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
      ],
      genres: [
        { genreId: '1', genreName: 'Genre 1' },
        { genreId: '2', genreName: 'Genre 2' },
        { genreId: '3', genreName: 'Genre 3' },
      ],
      duration: 30 + Math.floor(Math.random() * 120),
      premiereDate: new Date(),
      premiereTheatre: { theatreId: '1', theatreName: 'Theatre' },
      cast: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
        { actorId: '5', actorName: 'Actor 5', character: 'Character 5' },
        { actorId: '6', actorName: 'Actor 6', character: 'Character 6' },
        { actorId: '7', actorName: 'Actor 7', character: 'Character 7' },
        { actorId: '8', actorName: 'Actor 8', character: 'Character 8' },
        { actorId: '9', actorName: 'Actor 9', character: 'Character 9' },
        { actorId: '10', actorName: 'Actor 10', character: 'Character 10' },
      ],
      crew: [
        { workerId: '1', workerName: 'Crew 1', role: 'Role 1' },
        { workerId: '2', workerName: 'Crew 2', role: 'Role 2' },
        { workerId: '3', workerName: 'Crew 3', role: 'Role 3' },
        { workerId: '4', workerName: 'Crew 4', role: 'Role 4' },
        { workerId: '5', workerName: 'Crew 5', role: 'Role 5' },
        { workerId: '6', workerName: 'Crew 6', role: 'Role 6' },
        { workerId: '7', workerName: 'Crew 7', role: 'Role 7' },
        { workerId: '8', workerName: 'Crew 8', role: 'Role 8' },
        { workerId: '9', workerName: 'Crew 9', role: 'Role 9' },
        { workerId: '10', workerName: 'Crew 10', role: 'Role 10' },
      ],
      releases: [
        {
          releaseDate: new Date('2025-05-13'),
          theatres: [
            { theatreId: '1', theatreName: 'Theatre 1' },
            { theatreId: '2', theatreName: 'Theatre 2' },
          ],
        },
        {
          releaseDate: new Date('2025-05-12'),
          theatres: [
            { theatreId: '3', theatreName: 'Theatre 3' },
            { theatreId: '4', theatreName: 'Theatre 4' },
            { theatreId: '5', theatreName: 'Theatre 5' },
          ],
        },
        {
          releaseDate: new Date('2025-05-11'),
          theatres: [{ theatreId: '6', theatreName: 'Theatre 6' }],
        },
      ],
      socials: [
        {
          platform: 'instagram',
          socialIconUrl:
            'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/1024px-Instagram_icon.png',
          socialLink: 'https://url.com',
          socialName: 'tarantino',
        },
      ],
      comments: [
        {
          id: 1,
          author: {
            userId: 1,
            username: 'Jane Doe',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
          },
          date: new Date(),
          content: 'This is such a helpful post! Thanks for sharing',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 8,
        },
        {
          id: 2,
          author: {
            userId: 2,
            username: 'John Smith',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=3',
          },
          date: new Date(),
          content: 'I disagree with your take, but interesting perspective!',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 8,
        },
      ],
    },
    {
      playId: 9,
      title: 'Your Name',
      director: { directorId: '1', directorName: 'Makoto Shinkai' },
      synopsis:
        'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Perferendis, necessitatibus nulla minus aliquid incidunt consectetur ipsam pariatur quidem officiis cupiditate maxime placeat vel omnis aliquam! Eveniet adipisci sed veritatis tenetur.',
      releaseYear: 2017,
      rating: 1 + Math.round(Math.random() * 5 * 10) / 10,
      watchCount: Math.floor(Math.random() * (1_000_000 * 10)),
      reviewCount: Math.floor(Math.random() * (1_000_000 * 10)),
      posterImageUrl:
        'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/q719jXXEzOoYaps6babgKnONONX.jpg',
      mainActors: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
      ],
      genres: [
        { genreId: '1', genreName: 'Genre 1' },
        { genreId: '2', genreName: 'Genre 2' },
        { genreId: '3', genreName: 'Genre 3' },
      ],
      duration: 30 + Math.floor(Math.random() * 120),
      premiereDate: new Date(),
      premiereTheatre: { theatreId: '1', theatreName: 'Theatre' },
      cast: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
        { actorId: '5', actorName: 'Actor 5', character: 'Character 5' },
        { actorId: '6', actorName: 'Actor 6', character: 'Character 6' },
        { actorId: '7', actorName: 'Actor 7', character: 'Character 7' },
        { actorId: '8', actorName: 'Actor 8', character: 'Character 8' },
        { actorId: '9', actorName: 'Actor 9', character: 'Character 9' },
        { actorId: '10', actorName: 'Actor 10', character: 'Character 10' },
      ],
      crew: [
        { workerId: '1', workerName: 'Crew 1', role: 'Role 1' },
        { workerId: '2', workerName: 'Crew 2', role: 'Role 2' },
        { workerId: '3', workerName: 'Crew 3', role: 'Role 3' },
        { workerId: '4', workerName: 'Crew 4', role: 'Role 4' },
        { workerId: '5', workerName: 'Crew 5', role: 'Role 5' },
        { workerId: '6', workerName: 'Crew 6', role: 'Role 6' },
        { workerId: '7', workerName: 'Crew 7', role: 'Role 7' },
        { workerId: '8', workerName: 'Crew 8', role: 'Role 8' },
        { workerId: '9', workerName: 'Crew 9', role: 'Role 9' },
        { workerId: '10', workerName: 'Crew 10', role: 'Role 10' },
      ],
      releases: [
        {
          releaseDate: new Date('2025-05-13'),
          theatres: [
            { theatreId: '1', theatreName: 'Theatre 1' },
            { theatreId: '2', theatreName: 'Theatre 2' },
          ],
        },
        {
          releaseDate: new Date('2025-05-12'),
          theatres: [
            { theatreId: '3', theatreName: 'Theatre 3' },
            { theatreId: '4', theatreName: 'Theatre 4' },
            { theatreId: '5', theatreName: 'Theatre 5' },
          ],
        },
        {
          releaseDate: new Date('2025-05-11'),
          theatres: [{ theatreId: '6', theatreName: 'Theatre 6' }],
        },
      ],
      socials: [
        {
          platform: 'instagram',
          socialIconUrl:
            'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/1024px-Instagram_icon.png',
          socialLink: 'https://url.com',
          socialName: 'shinkaimakoto',
        },
      ],
      comments: [
        {
          id: 1,
          author: {
            userId: 1,
            username: 'Jane Doe',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
          },
          date: new Date(),
          content: 'This is such a helpful post! Thanks for sharing',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 9,
        },
        {
          id: 2,
          author: {
            userId: 2,
            username: 'John Smith',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=3',
          },
          date: new Date(),
          content: 'I disagree with your take, but interesting perspective!',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 9,
        },
      ],
    },
    {
      playId: 10,
      title: 'The Lord of the Rings: The return of the king',
      director: { directorId: '1', directorName: 'Peter Jackson' },
      synopsis:
        'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Perferendis, necessitatibus nulla minus aliquid incidunt consectetur ipsam pariatur quidem officiis cupiditate maxime placeat vel omnis aliquam! Eveniet adipisci sed veritatis tenetur.',
      releaseYear: 2003,
      rating: 1 + Math.round(Math.random() * 5 * 10) / 10,
      watchCount: Math.floor(Math.random() * (1_000_000 * 10)),
      reviewCount: Math.floor(Math.random() * (1_000_000 * 10)),
      posterImageUrl:
        'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg',
      mainActors: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
      ],
      genres: [
        { genreId: '1', genreName: 'Genre 1' },
        { genreId: '2', genreName: 'Genre 2' },
        { genreId: '3', genreName: 'Genre 3' },
      ],
      duration: 30 + Math.floor(Math.random() * 120),
      premiereDate: new Date(),
      premiereTheatre: { theatreId: '1', theatreName: 'Theatre' },
      cast: [
        { actorId: '1', actorName: 'Actor 1', character: 'Character 1' },
        { actorId: '2', actorName: 'Actor 2', character: 'Character 2' },
        { actorId: '3', actorName: 'Actor 3', character: 'Character 3' },
        { actorId: '4', actorName: 'Actor 4', character: 'Character 4' },
        { actorId: '5', actorName: 'Actor 5', character: 'Character 5' },
        { actorId: '6', actorName: 'Actor 6', character: 'Character 6' },
        { actorId: '7', actorName: 'Actor 7', character: 'Character 7' },
        { actorId: '8', actorName: 'Actor 8', character: 'Character 8' },
        { actorId: '9', actorName: 'Actor 9', character: 'Character 9' },
        { actorId: '10', actorName: 'Actor 10', character: 'Character 10' },
      ],
      crew: [
        { workerId: '1', workerName: 'Crew 1', role: 'Role 1' },
        { workerId: '2', workerName: 'Crew 2', role: 'Role 2' },
        { workerId: '3', workerName: 'Crew 3', role: 'Role 3' },
        { workerId: '4', workerName: 'Crew 4', role: 'Role 4' },
        { workerId: '5', workerName: 'Crew 5', role: 'Role 5' },
        { workerId: '6', workerName: 'Crew 6', role: 'Role 6' },
        { workerId: '7', workerName: 'Crew 7', role: 'Role 7' },
        { workerId: '8', workerName: 'Crew 8', role: 'Role 8' },
        { workerId: '9', workerName: 'Crew 9', role: 'Role 9' },
        { workerId: '10', workerName: 'Crew 10', role: 'Role 10' },
      ],
      releases: [
        {
          releaseDate: new Date('2025-05-13'),
          theatres: [
            { theatreId: '1', theatreName: 'Theatre 1' },
            { theatreId: '2', theatreName: 'Theatre 2' },
          ],
        },
        {
          releaseDate: new Date('2025-05-12'),
          theatres: [
            { theatreId: '3', theatreName: 'Theatre 3' },
            { theatreId: '4', theatreName: 'Theatre 4' },
            { theatreId: '5', theatreName: 'Theatre 5' },
          ],
        },
        {
          releaseDate: new Date('2025-05-11'),
          theatres: [{ theatreId: '6', theatreName: 'Theatre 6' }],
        },
      ],
      socials: [
        {
          platform: 'instagram',
          socialIconUrl:
            'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/1024px-Instagram_icon.png',
          socialLink: 'https://url.com',
          socialName: 'peter_jackson',
        },
      ],
      comments: [
        {
          id: 1,
          author: {
            userId: 1,
            username: 'Jane Doe',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=5',
          },
          date: new Date(),
          content: 'This is such a helpful post! Thanks for sharing',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 10,
        },
        {
          id: 2,
          author: {
            userId: 2,
            username: 'John Smith',
            profilePhotoUrl: 'https://i.pravatar.cc/150?img=3',
          },
          date: new Date(),
          content: 'I disagree with your take, but interesting perspective!',
          likes: Math.floor(Math.random() * 100),
          likedByUser: false,
          rating: 0.5 + Math.floor(Math.random() * 10) * 0.5,
          playId: 10,
        },
      ],
    },
  ];

  constructor() {}

  public getAllPlays(): Observable<PlayDetails[]> {
    return of(this.mockPlays);
  }

  public getPlayById(id: number): Observable<PlayDetails | undefined> {
    return of(this.mockPlays.find((mp) => mp.playId === id));
  }

  public getTrendingPlays(): Observable<PlayDetails[]> {
    const selectedPlays: PlayDetails[] = [];

    while (selectedPlays.length < 5) {
      const p =
        this.mockPlays[Math.floor(Math.random() * this.mockPlays.length)];

      if (selectedPlays.find((sp) => sp.playId === p.playId)) continue;

      selectedPlays.push(p);
    }

    return of(selectedPlays);
  }
}
