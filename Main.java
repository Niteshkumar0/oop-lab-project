
import courseOffering.*;
import java.io.*;
import java.lang.*;
import login.*;
import student.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Login l1 = new Login();
        int option = l1.Asking();

// <--------------------     LOGIN CODE HERE    ---------------------------------->
        //    TEACHER LOGIN SECTION
        if (option == 1) {
            TeacherLogin teacher = new TeacherLogin();
            teacher.detail();
            int checkingTeacher = teacher.checking();
            if (checkingTeacher == 1) {
                System.out.println("Teacher is valid account");
            } else {
                System.out.println("Teacher is invalid account");
            }
        } // STUDENT LOGIN SECTION
        else if (option == 2) {
            StudentLogin studentLogin = new StudentLogin();
            studentLogin.detail();
            int check = studentLogin.studentExist();
            if (check == 1) {
                Student student1 = new Student();
                student1.welcome(studentLogin.studentId);
                // student1.attemptQuiz();
            } else if (check == 0) {
                System.out.println("student is invalid!");
            }
        } else if (option == 3) {
            Course course = new Course();
            course.CourseOfferingList();
        }

    }
}
