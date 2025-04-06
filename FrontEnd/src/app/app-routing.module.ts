import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './components/login/login.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {DataGenerationComponent} from "./components/data-generation/data-generation.component";
import {DataUploadComponent} from "./components/data-upload/data-upload.component";
import {DataProcessingComponent} from "./components/data-processing/data-processing.component";
import {StudentManagementComponent} from "./components/student/student-management/student-management.component";
import {EditStudentComponent} from "./components/student/edit-student/edit-student.component";
import {ViewStudentComponent} from "./components/student/view-student/view-student.component";

const routes: Routes = [
    {path: 'login', component: LoginComponent},
    {path: '', component: LoginComponent},
    {path: 'dashboard', component: DashboardComponent},
    {path: 'data-generation', component: DataGenerationComponent},
    {path: 'data-upload', component: DataUploadComponent},
    {path: 'data-processing', component: DataProcessingComponent},
    {path: 'student-management', component: StudentManagementComponent},
    {path: 'edit-student/:id', component: EditStudentComponent},
    { path: 'student/view/:id', component: ViewStudentComponent }

];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
