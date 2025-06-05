package login;

import java.util.Scanner;
public class Login {
    Scanner input = new Scanner(System.in);
    public int Asking(){
        int option;
        System.out.println(" ==================== Student or Teacher =================");
        System.out.println(" 1. Teacher");
        System.out.println(" 2. Student ");
        System.out.println(" 3. Course Offering ");
        System.out.println(" 3. System ");
        System.out.println(" 4. Exit");
        System.out.print("Enter : ");
        option = input.nextInt();
        return option;
    }
}
