import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AuthInterceptor } from './auth/auth.interceptor';
import {SidebarComponent} from "./components/sidebar/sidebar.component";
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatListModule} from "@angular/material/list";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {DataGenerationComponent} from "./components/data-generation/data-generation.component";
import {DataUploadComponent} from "./components/data-upload/data-upload.component";
import {DataProcessingComponent} from "./components/data-processing/data-processing.component";

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        DashboardComponent,
        DataGenerationComponent,
        DataUploadComponent,
        DataProcessingComponent
    ],
    bootstrap: [AppComponent],
    imports: [
        BrowserModule,
        AppRoutingModule,
        ReactiveFormsModule,
        FormsModule,
        SidebarComponent,
        MatSidenavModule,
        MatToolbarModule,
        MatListModule,
        MatIconModule,
        MatButtonModule
    ],
    providers: [
        provideHttpClient(withInterceptorsFromDi()),
        AuthInterceptor,
        provideAnimationsAsync()
    ]
})
export class AppModule {}
