package login;
import  java.io.*;
import java.util.*;
public class StudentLogin {
    Scanner input = new Scanner(System.in);
    public String studentId;
    String password;
    public void detail(){
        System.out.println(" =============== Login to Student portal =============== ");
        System.out.println("student id : ");
        studentId = input.next();
        System.out.println("password : ");
        password = input.next();
    }

    public int studentExist() {
        try {
            File file = new File("student.txt");
            Scanner read = new Scanner(file);

            while (read.hasNextLine()) {
                String data = read.nextLine();
                String[] parts = data.split(",");
                if (parts.length >= 2) {
                    if (studentId.equals(parts[0].trim()) && password.equals(parts[1].trim())) {
                    return 1;
                }
            }
                }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
