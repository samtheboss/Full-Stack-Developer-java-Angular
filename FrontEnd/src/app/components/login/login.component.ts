import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/service/auth.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

    loginForm: FormGroup;

    constructor(
        private service: AuthService,
        private fb: FormBuilder,
        private router: Router
    ) { }

    ngOnInit() {
        // Clear old token if any
        localStorage.removeItem('JWT');

        this.loginForm = this.fb.group({
            username: ['', Validators.required],
            password: ['', Validators.required],
        });
    }

    login() {
        if (this.loginForm.invalid) return;

        const { username, password } = this.loginForm.value;
        this.service.login(username, password).subscribe({
            next: (response) => {
                const jwtToken = response.jwt_token;
                localStorage.setItem('JWT', jwtToken);
                this.router.navigate(['dashboard']);
            },
            error: (err) => {
                console.error('Login failed', err);
            }
        });
    }
}
