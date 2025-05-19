export interface Review {
  play: {
    title: string;
    date: {
      fullDate: Date;
      year: number;
      month: number;
      day: number;
    };
    posterUrl: string;
  };
  user: {
    name: string;
    avatarUrl: string;
  };
  rating: number;
  numLikes: number;
  reviewText: string;
  numComments: number;
}
