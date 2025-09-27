import { NgFor, NgIf } from '@angular/common';
import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
  forwardRef,
} from '@angular/core';
import {
  ControlValueAccessor,
  FormsModule,
  NG_VALUE_ACCESSOR,
} from '@angular/forms';

@Component({
  selector: 'app-select',
  imports: [NgIf, NgFor, FormsModule],
  templateUrl: './select.component.html',
  styleUrl: './select.component.scss',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SelectComponent),
      multi: true,
    },
  ],
})
export class SelectComponent
  implements ControlValueAccessor, OnChanges, OnInit
{
  @Input() label: string | null = null;
  @Input() placeholder: string = 'Selecione...';
  @Input() options: { value: string | number | null; label: string }[] = [];
  @Input() disabled: boolean = false;
  @Input() errorMessage: string = '';
  @Input() submitted: boolean = false;

  value: any;

  showDropdown = false;
  searchText = this.placeholder;
  filteredOptions: { value: any; label: string }[] = [];

  onChange = (value: any) => {};
  onTouched = () => {};

  ngOnInit() {
    this.filteredOptions = this.options;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['options']) {
      this.filteredOptions = this.options;
    }
  }

  get selectedLabel(): string {
    if (!this.options) return this.placeholder;
    const found = this.options.find((opt) => opt.value === this.value)?.label;
    return found ? found : this.placeholder;
  }

  onSearchBlur(): void {
    setTimeout(() => {
      this.showDropdown = false;
      this.searchText = this.selectedLabel;
    }, 150);
  }

  writeValue(value: any): void {
    this.value = value;
    this.searchText = this.selectedLabel;
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  toggleDropdown(): void {
    if (this.disabled) return;
    this.showDropdown = !this.showDropdown;
    if (this.showDropdown) {
      this.searchText = '';
      this.filteredOptions = this.options;
    }
  }

  selectOption(option: any): void {
    this.value = option.value;
    this.onChange(this.value);
    this.onTouched();
    this.showDropdown = false;
    this.searchText = this.selectedLabel;
  }

  filterOptions(searchText: string): void {
    const search = searchText.toLowerCase();
    this.filteredOptions = this.options.filter((opt) =>
      opt.label.toLowerCase().includes(search)
    );
  }
}
