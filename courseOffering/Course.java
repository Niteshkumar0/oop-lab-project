package courseOffering;
import java.io.*;
import  java.util.*;
public class Course {

    public void CourseOfferingList(){
        File file = new File("courseOffering.txt");

        {
            try {
                Scanner read = new Scanner(file);
                while(read.hasNextLine()){
                    String data = read.nextLine();
                    String[] splitData = data.split(",");
                    System.out.println("Course : "+splitData[0]);
                        System.out.println("      name : "+splitData[1]);
                        System.out.println("      instructor name : "+splitData[2]);
                        System.out.println("      credit hour : "+splitData[3]);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
