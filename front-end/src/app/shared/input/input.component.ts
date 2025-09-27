import { NgClass, NgIf } from '@angular/common';
import {
  Component,
  ElementRef,
  Input,
  ViewChild,
  forwardRef,
} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-input',
  imports: [NgClass, NgIf],
  templateUrl: './input.component.html',
  styleUrl: './input.component.scss',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputComponent),
      multi: true,
    },
  ],
})
export class InputComponent implements ControlValueAccessor {
  @Input() label: string | null = null;
  @Input() type: string = 'text';
  @Input() placeholder: string = '';
  @Input() errorMessage: string = '';
  @Input() submitted: boolean = false;
  @Input() autofocus: boolean = false;

  @ViewChild('inputRef') inputRef!: ElementRef<HTMLInputElement>;

  value: any = '';
  onChange = (value: any) => {};
  onTouched = () => {};

  writeValue(value: any): void {
    this.value = value;
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  onInputChange(event: any): void {
    const value = event.target.value;
    this.value = value;
    this.onChange(value);
    this.onTouched();
  }

  ngAfterViewInit(): void {
    if (this.autofocus && this.inputRef) {
      setTimeout(() => this.inputRef.nativeElement.focus());
    }
  }
}
