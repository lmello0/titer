import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'compactNumber',
})
export class CompactNumberPipe implements PipeTransform {
  transform(value: number | null | undefined): string {
    if (value == null || isNaN(value)) return '';

    return new Intl.NumberFormat('en', {
      notation: 'compact',
      compactDisplay: 'short',
    }).format(value);
  }
}
