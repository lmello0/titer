import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-modal',
  imports: [CommonModule],
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.css',
})
export class ModalComponent {
  @Input() isOpen = false;
  @Input() modalTitle = 'Modal Title';

  @Output() close = new EventEmitter();

  onClose() {
    this.isOpen = false;
    this.close.emit();
  }
}
