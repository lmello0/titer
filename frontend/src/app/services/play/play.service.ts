import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { PlayDetails, PlayStatus } from '../../shared/interfaces/play-details.interface';
import { Review } from '../../shared/interfaces/review.interface';
import { ReviewSortOption } from '../../shared/interfaces/review-sort-option.type';

interface PlayFilters {
  status?: PlayStatus;
  genreId?: string;
  minRating?: number;
  year?: number;
}

interface PlaySeed extends Omit<PlayDetails, 'aggregateRating' | 'rating' | 'totalReviewCount' | 'reviewCount'> {}

@Injectable({
  providedIn: 'root',
})
export class PlayService {
  private readonly plays: PlayDetails[] = this.seedPlays().map((play) => this.withDerivedFields(play));

  public getAllPlays(): Observable<PlayDetails[]> {
    return of(this.clonePlays(this.plays.filter((play) => play.status !== 'rejected')));
  }

  public getPlayById(id: number): Observable<PlayDetails | undefined> {
    const play = this.plays.find((candidate) => candidate.playId === id && candidate.status !== 'rejected');

    return of(play ? this.clonePlay(play) : undefined);
  }

  public getTrendingPlays(limit = 10): Observable<PlayDetails[]> {
    const sorted = this.getPublicPlays()
      .sort((a, b) => this.getLatestReviewDate(b).getTime() - this.getLatestReviewDate(a).getTime())
      .sort((a, b) => b.totalReviewCount - a.totalReviewCount)
      .slice(0, limit);

    return of(this.clonePlays(sorted));
  }

  public getPlaysByListType(type: string): Observable<PlayDetails[]> {
    const normalized = type.toLowerCase();
    let plays = this.getPublicPlays();

    switch (normalized) {
      case 'trending':
        plays = [...plays].sort((a, b) => b.totalReviewCount - a.totalReviewCount);
        break;
      case 'top-rated':
      case 'top_rated':
        plays = [...plays].sort((a, b) => b.aggregateRating - a.aggregateRating);
        break;
      case 'recent':
      case 'latest':
        plays = [...plays].sort((a, b) => b.releaseYear - a.releaseYear);
        break;
      case 'verified':
        plays = plays.filter((play) => play.status === 'verified');
        break;
      case 'unverified':
        plays = plays.filter((play) => play.status === 'unverified');
        break;
      default:
        break;
    }

    return of(this.clonePlays(plays));
  }

  public getFilteredPlays(filters: PlayFilters = {}): Observable<PlayDetails[]> {
    const filtered = this.getPublicPlays().filter((play) => {
      const statusMatch = filters.status ? play.status === filters.status : true;
      const genreMatch = filters.genreId ? play.genres.some((genre) => genre.genreId === filters.genreId) : true;
      const ratingMatch = filters.minRating ? play.aggregateRating >= filters.minRating : true;
      const yearMatch = filters.year ? play.releaseYear === filters.year : true;

      return statusMatch && genreMatch && ratingMatch && yearMatch;
    });

    return of(this.clonePlays(filtered));
  }

  public searchPlays(searchTerm: string): Observable<PlayDetails[]> {
    const normalizedTerm = searchTerm.toLowerCase().trim();

    if (!normalizedTerm) {
      return this.getAllPlays();
    }

    const matches = this.getPublicPlays().filter((play) => {
      const inTitle = play.title.toLowerCase().includes(normalizedTerm);
      const inPlaywright = play.playwright.toLowerCase().includes(normalizedTerm);
      const inGenre = play.genres.some((genre) => genre.genreName.toLowerCase().includes(normalizedTerm));

      return inTitle || inPlaywright || inGenre;
    });

    return of(this.clonePlays(matches));
  }

  public getPlayReviews(playId: number, sortBy: ReviewSortOption = 'MOST_RECENT'): Observable<Review[]> {
    const play = this.plays.find((candidate) => candidate.playId === playId);

    if (!play) {
      return of([]);
    }

    return of(this.sortReviews(play.comments, sortBy).map((review) => ({ ...review })));
  }

  public getRecentReviews(limit = 6): Observable<Review[]> {
    const reviews = this.getPublicPlays()
      .flatMap((play) => play.comments.map((comment) => ({ ...comment })))
      .sort((a, b) => b.date.getTime() - a.date.getTime())
      .slice(0, limit);

    return of(reviews);
  }

  public addReview(playId: number, review: Omit<Review, 'id' | 'date' | 'likes' | 'likedByUser'>): Observable<Review | undefined> {
    const playIndex = this.plays.findIndex((candidate) => candidate.playId === playId);

    if (playIndex < 0) {
      return of(undefined);
    }

    const play = this.plays[playIndex];
    const nextReviewId = play.comments.length > 0 ? Math.max(...play.comments.map((comment) => comment.id)) + 1 : 1;

    const normalizedReview: Review = {
      ...review,
      id: nextReviewId,
      date: new Date(),
      likes: 0,
      likedByUser: false,
    };

    play.comments = [normalizedReview, ...play.comments];
    this.plays[playIndex] = this.withDerivedFields(play);

    return of({ ...normalizedReview });
  }

  private getPublicPlays(): PlayDetails[] {
    return this.plays.filter((play) => play.status !== 'rejected');
  }

  private sortReviews(reviews: Review[], sortBy: ReviewSortOption): Review[] {
    const sorted = [...reviews];

    switch (sortBy) {
      case 'TOP_RATED':
        sorted.sort((a, b) => b.rating - a.rating || b.likes - a.likes);
        break;
      case 'MOST_LIKED':
        sorted.sort((a, b) => b.likes - a.likes || b.date.getTime() - a.date.getTime());
        break;
      case 'MOST_RECENT':
      default:
        sorted.sort((a, b) => b.date.getTime() - a.date.getTime());
        break;
    }

    return sorted;
  }

  private getLatestReviewDate(play: PlayDetails): Date {
    if (play.comments.length === 0) {
      return new Date(play.releaseYear, 0, 1);
    }

    return [...play.comments].sort((a, b) => b.date.getTime() - a.date.getTime())[0].date;
  }

  private withDerivedFields(seed: PlaySeed | PlayDetails): PlayDetails {
    const aggregateRating = this.computeAggregateRating(seed.comments);
    const totalReviewCount = seed.comments.length;

    return {
      ...seed,
      aggregateRating,
      totalReviewCount,
      rating: aggregateRating,
      reviewCount: totalReviewCount,
      comments: [...seed.comments].sort((a, b) => b.date.getTime() - a.date.getTime()),
    };
  }

  private computeAggregateRating(reviews: Review[]): number {
    if (reviews.length === 0) {
      return 0;
    }

    const sum = reviews.reduce((acc, review) => acc + review.rating, 0);

    return Math.round((sum / reviews.length) * 10) / 10;
  }

  private clonePlay(play: PlayDetails): PlayDetails {
    return {
      ...play,
      comments: play.comments.map((comment) => ({ ...comment })),
      cast: play.cast.map((actor) => ({ ...actor })),
      crew: play.crew.map((worker) => ({ ...worker })),
      mainActors: play.mainActors.map((actor) => ({ ...actor })),
      genres: play.genres.map((genre) => ({ ...genre })),
      releases: play.releases.map((release) => ({
        ...release,
        releaseDate: new Date(release.releaseDate),
        theatres: release.theatres.map((theatre) => ({ ...theatre })),
      })),
      socials: play.socials.map((social) => ({ ...social })),
    };
  }

  private clonePlays(plays: PlayDetails[]): PlayDetails[] {
    return plays.map((play) => this.clonePlay(play));
  }

  private seedPlays(): PlaySeed[] {
    return [
      {
        playId: 1,
        title: 'Hamlet',
        playwright: 'William Shakespeare',
        director: { directorId: 'd1', directorName: 'Sam Mendes' },
        synopsis:
          'A prince wrestles with grief, political corruption, and revenge after his father is murdered. This production highlights court intrigue with a modern staging.',
        releaseYear: 2024,
        watchCount: 128_400,
        posterImageUrl: 'https://images.unsplash.com/photo-1503095396549-807759245b35?auto=format&fit=crop&w=600&q=80',
        status: 'verified',
        originalLanguage: 'English',
        productionCompany: 'Royal Theatre Collective',
        submittedBy: 'jane.stagefan',
        verifiedBy: 'trusted.curator',
        mainActors: [
          { actorId: 'a1', actorName: 'David Oyelowo', character: 'Hamlet' },
          { actorId: 'a2', actorName: 'Ruth Wilson', character: 'Ophelia' },
        ],
        genres: [
          { genreId: 'g1', genreName: 'Tragedy' },
          { genreId: 'g2', genreName: 'Drama' },
        ],
        duration: 172,
        premiereDate: new Date('2024-03-14T19:30:00Z'),
        premiereTheatre: { theatreId: 't1', theatreName: 'National Stage House' },
        cast: [
          { actorId: 'a1', actorName: 'David Oyelowo', character: 'Hamlet' },
          { actorId: 'a2', actorName: 'Ruth Wilson', character: 'Ophelia' },
          { actorId: 'a3', actorName: 'Mark Rylance', character: 'Claudius' },
        ],
        crew: [
          { workerId: 'w1', workerName: 'Erin Shaw', role: 'Lighting Design' },
          { workerId: 'w2', workerName: 'Noah Carter', role: 'Costume Design' },
        ],
        releases: [
          {
            releaseDate: new Date('2024-03-14T19:30:00Z'),
            theatres: [{ theatreId: 't1', theatreName: 'National Stage House' }],
          },
          {
            releaseDate: new Date('2025-01-22T19:30:00Z'),
            theatres: [{ theatreId: 't2', theatreName: 'Broadgate Playhouse' }],
          },
        ],
        socials: [
          {
            platform: 'instagram',
            socialIconUrl: 'https://cdn.simpleicons.org/instagram/f2f6ff',
            socialLink: 'https://instagram.com/hamletstage',
            socialName: 'hamletstage',
          },
        ],
        comments: [
          {
            id: 1,
            playId: 1,
            author: { userId: 1, username: 'theatre.maya', profilePhotoUrl: 'https://i.pravatar.cc/150?img=13' },
            date: new Date('2026-02-20T20:00:00Z'),
            title: 'Stunning lead performance',
            content:
              'The lead performance carries a precise emotional progression, and the staging choices make the court scenes feel tense without becoming noisy. The final act lands with real weight and restraint.',
            likes: 54,
            likedByUser: false,
            rating: 4.5,
            numComments: 6,
          },
          {
            id: 2,
            playId: 1,
            author: { userId: 2, username: 'mainstageleo', profilePhotoUrl: 'https://i.pravatar.cc/150?img=22' },
            date: new Date('2026-02-10T19:10:00Z'),
            title: 'Excellent but long',
            content:
              'Direction and cast quality are excellent, though pacing in the middle section could be tighter. The production design is consistently strong and the ensemble scenes remain sharp throughout.',
            likes: 31,
            likedByUser: true,
            rating: 4,
            numComments: 2,
          },
        ],
      },
      {
        playId: 2,
        title: 'Death of a Salesman',
        playwright: 'Arthur Miller',
        director: { directorId: 'd2', directorName: 'Kenny Leon' },
        synopsis:
          'Willy Loman confronts his collapsing sense of identity while his family attempts to navigate financial pressure and inherited expectations.',
        releaseYear: 2023,
        watchCount: 94_220,
        posterImageUrl: 'https://images.unsplash.com/photo-1460723237483-7a6dc9d0b212?auto=format&fit=crop&w=600&q=80',
        status: 'verified',
        originalLanguage: 'English',
        productionCompany: 'Downtown Repertory',
        submittedBy: 'broadway.sam',
        verifiedBy: 'trusted.curator',
        mainActors: [
          { actorId: 'a4', actorName: 'Wendell Pierce', character: 'Willy Loman' },
          { actorId: 'a5', actorName: 'Sharon D Clarke', character: 'Linda Loman' },
        ],
        genres: [
          { genreId: 'g2', genreName: 'Drama' },
          { genreId: 'g3', genreName: 'Classic' },
        ],
        duration: 155,
        premiereDate: new Date('2023-05-20T19:00:00Z'),
        premiereTheatre: { theatreId: 't3', theatreName: 'Hudson Square Theatre' },
        cast: [
          { actorId: 'a4', actorName: 'Wendell Pierce', character: 'Willy Loman' },
          { actorId: 'a5', actorName: 'Sharon D Clarke', character: 'Linda Loman' },
          { actorId: 'a6', actorName: 'Andre Holland', character: 'Biff Loman' },
        ],
        crew: [
          { workerId: 'w3', workerName: 'Nina Dunn', role: 'Projection Design' },
          { workerId: 'w4', workerName: 'Tom White', role: 'Sound Design' },
        ],
        releases: [
          {
            releaseDate: new Date('2023-05-20T19:00:00Z'),
            theatres: [{ theatreId: 't3', theatreName: 'Hudson Square Theatre' }],
          },
        ],
        socials: [
          {
            platform: 'x',
            socialIconUrl: 'https://cdn.simpleicons.org/x/f2f6ff',
            socialLink: 'https://x.com/salesmanrevival',
            socialName: 'salesmanrevival',
          },
        ],
        comments: [
          {
            id: 1,
            playId: 2,
            author: { userId: 4, username: 'rowgwen', profilePhotoUrl: 'https://i.pravatar.cc/150?img=17' },
            date: new Date('2026-02-18T20:15:00Z'),
            title: 'Powerful central duo',
            content:
              'The central duo performs with conviction and keeps the family tension alive in every scene. It is emotionally heavy, but the script adaptation remains clear and accessible.',
            likes: 22,
            likedByUser: false,
            rating: 4,
            numComments: 3,
          },
          {
            id: 2,
            playId: 2,
            author: { userId: 8, username: 'augustnights', profilePhotoUrl: 'https://i.pravatar.cc/150?img=29' },
            date: new Date('2026-02-03T18:05:00Z'),
            title: 'Strong staging choices',
            content:
              'Set transitions are smooth and purposeful, and lighting supports the memory sequences without distracting from the actors. I would recommend this production to any first-time viewer.',
            likes: 19,
            likedByUser: false,
            rating: 4.5,
            numComments: 1,
          },
        ],
      },
      {
        playId: 3,
        title: 'Waiting for Godot',
        playwright: 'Samuel Beckett',
        director: { directorId: 'd3', directorName: 'Yael Farber' },
        synopsis:
          'Two companions wait in uncertainty while language, routine, and existential fatigue shape their day. This new production emphasizes intimacy and silence.',
        releaseYear: 2025,
        watchCount: 56_400,
        posterImageUrl: 'https://images.unsplash.com/photo-1516307365426-bea591f05011?auto=format&fit=crop&w=600&q=80',
        status: 'unverified',
        originalLanguage: 'English',
        productionCompany: 'City Fringe Lab',
        submittedBy: 'newstage.olive',
        verifiedBy: null,
        mainActors: [
          { actorId: 'a7', actorName: 'Bill Nighy', character: 'Vladimir' },
          { actorId: 'a8', actorName: 'Ncuti Gatwa', character: 'Estragon' },
        ],
        genres: [
          { genreId: 'g4', genreName: 'Experimental' },
          { genreId: 'g2', genreName: 'Drama' },
        ],
        duration: 138,
        premiereDate: new Date('2025-10-03T19:00:00Z'),
        premiereTheatre: { theatreId: 't4', theatreName: 'Black Box East' },
        cast: [
          { actorId: 'a7', actorName: 'Bill Nighy', character: 'Vladimir' },
          { actorId: 'a8', actorName: 'Ncuti Gatwa', character: 'Estragon' },
        ],
        crew: [{ workerId: 'w5', workerName: 'Mira Patel', role: 'Stage Manager' }],
        releases: [
          {
            releaseDate: new Date('2025-10-03T19:00:00Z'),
            theatres: [{ theatreId: 't4', theatreName: 'Black Box East' }],
          },
        ],
        socials: [
          {
            platform: 'instagram',
            socialIconUrl: 'https://cdn.simpleicons.org/instagram/f2f6ff',
            socialLink: 'https://instagram.com/godotnewrun',
            socialName: 'godotnewrun',
          },
        ],
        comments: [
          {
            id: 1,
            playId: 3,
            author: { userId: 3, username: 'houseleft', profilePhotoUrl: 'https://i.pravatar.cc/150?img=30' },
            date: new Date('2026-02-25T22:30:00Z'),
            title: 'Bold and strange',
            content:
              'The performers sustain the absurd rhythm with precision, and the sparse set design keeps attention on timing and gesture. This is not for everyone, but it feels fresh and confident.',
            likes: 12,
            likedByUser: false,
            rating: 3.5,
            numComments: 0,
          },
        ],
      },
      {
        playId: 4,
        title: 'A Raisin in the Sun',
        playwright: 'Lorraine Hansberry',
        director: { directorId: 'd4', directorName: 'Phylicia Rashad' },
        synopsis:
          'A family negotiates ambition, dignity, and generational conflict after an insurance payment reshapes their choices.',
        releaseYear: 2022,
        watchCount: 87_250,
        posterImageUrl: 'https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?auto=format&fit=crop&w=600&q=80',
        status: 'verified',
        originalLanguage: 'English',
        productionCompany: 'Harbor Stage',
        submittedBy: 'rowgwen',
        verifiedBy: 'trusted.curator',
        mainActors: [
          { actorId: 'a9', actorName: 'Anika Noni Rose', character: 'Ruth Younger' },
          { actorId: 'a10', actorName: 'LaChanze', character: 'Lena Younger' },
        ],
        genres: [
          { genreId: 'g2', genreName: 'Drama' },
          { genreId: 'g3', genreName: 'Classic' },
        ],
        duration: 148,
        premiereDate: new Date('2022-09-09T19:00:00Z'),
        premiereTheatre: { theatreId: 't5', theatreName: 'Majestic Circle Theatre' },
        cast: [
          { actorId: 'a9', actorName: 'Anika Noni Rose', character: 'Ruth Younger' },
          { actorId: 'a10', actorName: 'LaChanze', character: 'Lena Younger' },
        ],
        crew: [{ workerId: 'w6', workerName: 'Ian Brooks', role: 'Choreography' }],
        releases: [
          {
            releaseDate: new Date('2022-09-09T19:00:00Z'),
            theatres: [{ theatreId: 't5', theatreName: 'Majestic Circle Theatre' }],
          },
        ],
        socials: [
          {
            platform: 'facebook',
            socialIconUrl: 'https://cdn.simpleicons.org/facebook/f2f6ff',
            socialLink: 'https://facebook.com/raisinproduction',
            socialName: 'raisinproduction',
          },
        ],
        comments: [
          {
            id: 1,
            playId: 4,
            author: { userId: 11, username: 'ticketpatch', profilePhotoUrl: 'https://i.pravatar.cc/150?img=33' },
            date: new Date('2026-02-11T21:00:00Z'),
            title: 'Emotionally grounded',
            content:
              'The ensemble is balanced and the family scenes feel lived in rather than staged. It remains one of the most affecting productions in the current season and deserves the strong audience response.',
            likes: 44,
            likedByUser: false,
            rating: 4.5,
            numComments: 5,
          },
        ],
      },
      {
        playId: 5,
        title: 'The Glass Menagerie',
        playwright: 'Tennessee Williams',
        director: { directorId: 'd5', directorName: 'Jamie Lloyd' },
        synopsis:
          'A memory play centered on family fragility, control, and longing. This adaptation uses minimalist staging and atmospheric sound cues.',
        releaseYear: 2025,
        watchCount: 61_005,
        posterImageUrl: 'https://images.unsplash.com/photo-1497032628192-86f99bcd76bc?auto=format&fit=crop&w=600&q=80',
        status: 'unverified',
        originalLanguage: 'English',
        productionCompany: 'Lower West Ensemble',
        submittedBy: 'mainstageleo',
        verifiedBy: null,
        mainActors: [
          { actorId: 'a11', actorName: 'Tuppence Middleton', character: 'Laura' },
          { actorId: 'a12', actorName: 'Paul Mescal', character: 'Tom' },
        ],
        genres: [
          { genreId: 'g2', genreName: 'Drama' },
          { genreId: 'g3', genreName: 'Classic' },
        ],
        duration: 132,
        premiereDate: new Date('2025-11-18T19:00:00Z'),
        premiereTheatre: { theatreId: 't6', theatreName: 'Riverfront Hall' },
        cast: [
          { actorId: 'a11', actorName: 'Tuppence Middleton', character: 'Laura' },
          { actorId: 'a12', actorName: 'Paul Mescal', character: 'Tom' },
        ],
        crew: [{ workerId: 'w7', workerName: 'Ada Kim', role: 'Sound Design' }],
        releases: [
          {
            releaseDate: new Date('2025-11-18T19:00:00Z'),
            theatres: [{ theatreId: 't6', theatreName: 'Riverfront Hall' }],
          },
        ],
        socials: [
          {
            platform: 'instagram',
            socialIconUrl: 'https://cdn.simpleicons.org/instagram/f2f6ff',
            socialLink: 'https://instagram.com/menagerielive',
            socialName: 'menagerielive',
          },
        ],
        comments: [
          {
            id: 1,
            playId: 5,
            author: { userId: 7, username: 'curtainline', profilePhotoUrl: 'https://i.pravatar.cc/150?img=39' },
            date: new Date('2026-01-29T20:45:00Z'),
            title: 'Quiet and precise',
            content:
              'This is a restrained interpretation that trusts the text and the performers. The emotional payoff is subtle but cumulative, and the final monologue is delivered with excellent control.',
            likes: 16,
            likedByUser: false,
            rating: 4,
            numComments: 1,
          },
        ],
      },
      {
        playId: 6,
        title: 'The Seagull',
        playwright: 'Anton Chekhov',
        director: { directorId: 'd6', directorName: 'Ivo van Hove' },
        synopsis:
          'An unverified submission that was rejected after review for incomplete metadata and conflicting release details.',
        releaseYear: 2026,
        watchCount: 4_020,
        posterImageUrl: 'https://images.unsplash.com/photo-1460723237483-7a6dc9d0b212?auto=format&fit=crop&w=600&q=80',
        status: 'rejected',
        originalLanguage: 'Russian',
        productionCompany: 'Unknown',
        submittedBy: 'anonymous.submitter',
        verifiedBy: 'trusted.curator',
        mainActors: [],
        genres: [{ genreId: 'g2', genreName: 'Drama' }],
        duration: 140,
        premiereDate: new Date('2026-01-06T19:00:00Z'),
        premiereTheatre: { theatreId: 't7', theatreName: 'Undisclosed Venue' },
        cast: [],
        crew: [],
        releases: [],
        socials: [],
        comments: [],
      },
      {
        playId: 7,
        title: 'The Seagull',
        playwright: 'Anton Chekhov',
        director: { directorId: 'd6', directorName: 'Ivo van Hove' },
        synopsis:
          'An unverified submission that was rejected after review for incomplete metadata and conflicting release details.',
        releaseYear: 2026,
        watchCount: 4_020,
        posterImageUrl: 'https://images.unsplash.com/photo-1460723237483-7a6dc9d0b212?auto=format&fit=crop&w=600&q=80',
        status: 'unverified',
        originalLanguage: 'Russian',
        productionCompany: 'Unknown',
        submittedBy: 'anonymous.submitter',
        verifiedBy: 'trusted.curator',
        mainActors: [],
        genres: [{ genreId: 'g2', genreName: 'Drama' }],
        duration: 140,
        premiereDate: new Date('2026-01-06T19:00:00Z'),
        premiereTheatre: { theatreId: 't7', theatreName: 'Undisclosed Venue' },
        cast: [],
        crew: [],
        releases: [],
        socials: [],
        comments: [],
      },
      {
        playId: 8,
        title: 'The Seagull',
        playwright: 'Anton Chekhov',
        director: { directorId: 'd6', directorName: 'Ivo van Hove' },
        synopsis:
          'An unverified submission that was rejected after review for incomplete metadata and conflicting release details.',
        releaseYear: 2026,
        watchCount: 4_020,
        posterImageUrl: 'https://images.unsplash.com/photo-1460723237483-7a6dc9d0b212?auto=format&fit=crop&w=600&q=80',
        status: 'verified',
        originalLanguage: 'Russian',
        productionCompany: 'Unknown',
        submittedBy: 'anonymous.submitter',
        verifiedBy: 'trusted.curator',
        mainActors: [],
        genres: [{ genreId: 'g2', genreName: 'Drama' }],
        duration: 140,
        premiereDate: new Date('2026-01-06T19:00:00Z'),
        premiereTheatre: { theatreId: 't7', theatreName: 'Undisclosed Venue' },
        cast: [],
        crew: [],
        releases: [],
        socials: [],
        comments: [],
      },
    ];
  }
}
