
import courseOffering.*;
import java.io.*;
import java.lang.*;
import login.*;
import student.*;
import teacher.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Login l1 = new Login();
        int option = l1.Asking();

// <--------------------     LOGIN CODE HERE    ---------------------------------->
        //    TEACHER LOGIN SECTION
        if (option == 1) {
            TeacherLogin teacherlogin = new TeacherLogin();
            teacherlogin.detail();
            int checkingTeacher = teacherlogin.checking();
            if (checkingTeacher == 1) {
               Teacher teacher = new Teacher();
               teacher.welcome(teacherlogin.username);
            } else {
                System.out.println("Teacher is invalid account");
            }
        }
         // STUDENT LOGIN SECTION
        else if (option == 2) {
            StudentLogin studentLogin = new StudentLogin();
            studentLogin.detail();
            int check = studentLogin.studentExist();
            if (check == 1) {
                Student student1 = new Student();
                student1.welcome(studentLogin.studentId);
            } else if (check == 0) {
                System.out.println("student is invalid!");
            }
        }
         else if (option == 3) {
            Course course = new Course();
            course.CourseOfferingList();
        }

    }
}
