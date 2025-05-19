export interface Comment {
  id: number;
  author: string;
  profilePhotoUrl: string;
  date: Date;
  content: string;
  likes: number;
  likedByUser: boolean;
}
