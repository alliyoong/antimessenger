import { Output, Directive, HostBinding, HostListener, EventEmitter } from '@angular/core';
import { Observable, Observer, Subject, catchError, concatMap, from, of, tap } from 'rxjs';

@Directive({
  selector: '[appDragndrop]'
})
export class DragndropDirective {
  @Output() fileDropped = new EventEmitter<any>();

  constructor() { }

  @HostListener('dragover', ['$event']) public onDragOver(evt: Event){
    evt.preventDefault();
    evt.stopPropagation();
  }

  @HostListener('dragleave', ['$event']) public onDragLeave(evt: Event){
    evt.preventDefault();
    evt.stopPropagation();
  }

  @HostListener('drop', ['$event']) public onDrop(evt: DragEvent){
    evt.preventDefault();
    evt.stopPropagation();
    const items = Array.from(evt.dataTransfer!.files);
    if (items.length > 0) {
      const file = items.slice(-1);
      this.fileDropped.emit(file);
    }
  }
  
}
