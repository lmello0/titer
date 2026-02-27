export type ReviewSortOption = 'MOST_RECENT' | 'TOP_RATED' | 'MOST_LIKED';

export const REVIEW_SORT_OPTIONS: { label: string; value: ReviewSortOption }[] = [
  { label: 'Most Recent', value: 'MOST_RECENT' },
  { label: 'Top Rated', value: 'TOP_RATED' },
  { label: 'Most Liked', value: 'MOST_LIKED' },
];
