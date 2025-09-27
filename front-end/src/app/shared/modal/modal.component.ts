import { NgClass, NgIf, NgStyle } from '@angular/common';
import { Component, EventEmitter, HostListener, Input, Output } from '@angular/core';

@Component({
  selector: 'app-modal',
  imports: [
    NgClass,
    NgStyle,
    NgIf
  ],
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.scss',
})
export class ModalComponent {
  @Input() title: string = 'Confirmação';
  @Input() isOpen: boolean = false;
  @Input() modalSize:
    | 'modal-sm'
    | 'modal-lg'
    | 'modal-xl'
    | 'modal-fullscreen'
    | '' = '';
  @Input() scrollable: 'modal-dialog-scrollable' | '' = '';
  @Input() customFooter: boolean = false;
  @Input() showFooter: boolean = true;
  @Input() fade: boolean = true;
  @Input() closeOnBackdrop: boolean = true;

  @Output() onClose = new EventEmitter<void>();
  @Output() onConfirm = new EventEmitter<void>();

  close(): void {
    this.onClose.emit();
  }

  confirm(): void {
    this.onConfirm.emit();
  }

  @HostListener('document:keydown.escape', ['$event'])
  onEscKey(event: KeyboardEvent) {
    if (this.isOpen) this.close();
  }
}
