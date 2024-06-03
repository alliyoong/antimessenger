import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'booleanTransform'
})
export class BooleanTransformPipe implements PipeTransform {

  transform(value: any, ...args: unknown[]): boolean {
    return value ? true : false;
  }

}
