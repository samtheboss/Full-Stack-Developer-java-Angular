import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import {DataGenerationComponent} from "./components/data-generation/data-generation.component";
import {DataUploadComponent} from "./components/data-upload/data-upload.component";
import {DataProcessingComponent} from "./components/data-processing/data-processing.component";

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', component: LoginComponent },
  {path:'dashboard', component: DashboardComponent},
  { path: 'data-generation', component: DataGenerationComponent },
  { path: 'data-upload', component: DataUploadComponent },
  { path: 'data-processing', component: DataProcessingComponent }


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
