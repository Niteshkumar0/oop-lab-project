package login;
import java.io.*;
import java.util.Scanner;
public class TeacherLogin {
    Scanner input = new Scanner(System.in);
    String username;
    private int pass;
    public void detail(){
        System.out.println(" =============== Welcome to Teacher portal =============== ");
        System.out.println("username : ");
        username = input.next();
        System.out.println("password : ");
        pass = input.nextInt();
    }

    public int checking(){
       try {
            File file = new File("teacher.txt");
            Scanner read = new Scanner(file);
            while(read.hasNextLine()){
                String line = read.nextLine();
                String[] parts = line.split(",");
                int password = Integer.parseInt(parts[1]);
                if((username.equals(parts[0])) && (password == pass)){
                    return 1;
                }else{
                    return 0;
                }

            }
       }catch(Exception e) {
            System.out.println(e);
        }

       return 0;

    }
}
