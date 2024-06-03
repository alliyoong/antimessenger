import { AbstractControl, FormGroupDirective, NgForm } from "@angular/forms";
import { ErrorStateMatcher } from "@angular/material/core";

export class CustomErrorMatcher implements ErrorStateMatcher{
    isErrorState(control: AbstractControl<any, any> | null, form: FormGroupDirective | NgForm | null): boolean {
        // return !!(control && control.dirty && control.invalid);
        return !!(control && control.touched && control.invalid);
    }
}