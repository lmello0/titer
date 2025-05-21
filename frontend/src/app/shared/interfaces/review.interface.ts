import { User } from './user.interface';

export interface Review {
  id: number;
  author: User;
  date: Date;
  content: string;
  likes: number;
  likedByUser: boolean;
  rating: number;
  playId: number;
}
