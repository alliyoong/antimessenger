import { AbstractControl, FormGroup } from "@angular/forms";

export class CustomValidator{
    static validatePassword(control: AbstractControl){
        const value = control.value as string;
        const pattern = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%*.-_?]).{8,30}$/;
        return pattern.test(value) ? null : {invalidPassword: true};
    }

    static validatePhoneNumber(control: AbstractControl){
        const value = control.value as string;
        if(value) {
            const pattern = /^[0-9]{8,10}$/;
            return pattern.test(value) ? null : {invalidPhoneNumber: true};
        }
        return null;
    }
    
    static validateMatchPassword(control: FormGroup){
        const password = control.get('password')?.value;
        const confirmPassword = control.get('confirmPassword')?.value;
        const confirmControl = control.get('confirmPassword');
        const currentError = confirmControl!.errors;
        if (password !== confirmPassword) {
            confirmControl!.setErrors({...currentError, missmatchPassword: true});
            return {missmatchPassword: true};
        } else {
            if(currentError && Object.keys(currentError).length === 1 && 'missmatchPassword' in currentError!) {
                confirmControl?.setErrors(null);
            } else {
                confirmControl?.setErrors(currentError);
            }
            return null;
        }    
    }
}