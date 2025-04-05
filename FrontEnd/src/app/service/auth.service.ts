import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const BASE_URL = [' http://localhost:8081/api/auth/']

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private http: HttpClient
  ) { }

  signup(signupRequest: any): Observable<any> {
    return this.http.post(BASE_URL + "sign-up", signupRequest)
  }

  login22(loginRequest: any): Observable<any> {
    return this.http.post(BASE_URL + "login", loginRequest)
  }

  login(username: string, password: string): Observable<any> {
    const loginRequest = { username, password };
    return this.http.post(BASE_URL + "login", loginRequest);
  }


  hello(): Observable<any> {
    return this.http.get(BASE_URL + 'api/hello', {
      headers: this.createAuthorizationHeader()
    });
  }

  private createAuthorizationHeader() {
    const jwtToken = localStorage.getItem('jwt_token');
    if (jwtToken) {
      return new HttpHeaders().set(
        'Authorization', 'Bearer ' + jwtToken
      )
    } else {
      console.log("JWT token not found in the Local Storage");
    }
    return null;
  }

}
