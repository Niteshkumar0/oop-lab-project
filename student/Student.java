package student;

import java.io.*;
import java.util.*;

public class Student {

    int courseChoice;
    Scanner read;
    Scanner input = new Scanner(System.in);
    String studentId;

    public void welcome(String val) {
        studentId = val;
        System.out.println(" =============== Welcome to Student Portal " + val + " ==============");
        showCourse();
        attemptQuiz();
    }

    //<------------------ SHOW COURSES ----------------->
    public void showCourse() {
        int count = 1;
        try {
            System.out.println(" -> YOUR COURSES");
            File file = new File("question.txt");
            read = new Scanner(file);
            while (read.hasNextLine()) {
                String data = read.nextLine();
                if (data.startsWith("#")) {
                    String[] split = data.split(";");
                    System.out.println("   " + count + ") " + split[1]);
                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("");
        System.out.println("");
    }

    public int attemptQuiz() {
        System.out.print("Which course quiz? : ");
        courseChoice = input.nextInt();
        input.nextLine();

        int totalQuestions = 0;
        int correctAnswers = 0;
        boolean quizStarted = false;
        String quizId = "";

        try {
            File file = new File("question.txt");
            Scanner read = new Scanner(file);

            while (read.hasNextLine()) {
                String rawLine = read.nextLine();
                String line = rawLine.trim();

                if (!quizStarted) {
                    if (line.startsWith("@QUIZ;") && line.contains("QuizID=")) {
                        quizId = line.substring(line.indexOf("QuizID=") + 7).trim();
                    }
                    if (line.equals(String.valueOf(courseChoice))) {
                        quizStarted = true;

                        if (hasStudentAttemptedQuiz(studentId, quizId)) {
                            System.out.println("⚠ You have already taken this quiz (Quiz ID: " + quizId + ").");
                            read.close();
                            return 0;
                        }
                    }
                    continue;
                }

                if (line.isEmpty() || line.startsWith("@QUIZ;") || line.startsWith("#COURSE;")) {
                    continue;
                }

                if (line.equals("0")) {
                    break;
                }

                String questionText = line;

                String optionsLine = "";
                while (read.hasNextLine()) {
                    String maybeOpt = read.nextLine().trim();
                    if (maybeOpt.isEmpty() || maybeOpt.startsWith("@QUIZ;") || maybeOpt.startsWith("#COURSE;")) {
                        continue;
                    }
                    optionsLine = maybeOpt;
                    break;
                }
                if (optionsLine.isEmpty()) {
                    System.out.println("⚠ Malformed file: missing options after question: " + questionText);
                    break;
                }
                String[] options = optionsLine.split(",");

                String answerLine = "";
                while (read.hasNextLine()) {
                    String maybeAns = read.nextLine().trim();
                    if (maybeAns.isEmpty() || maybeAns.startsWith("@QUIZ;") || maybeAns.startsWith("#COURSE;")) {
                        continue;
                    }
                    answerLine = maybeAns;
                    break;
                }
                if (!answerLine.startsWith("Answer=")) {
                    System.out.println("⚠ Malformed file: expected 'Answer=' but got: " + answerLine);
                    break;
                }

                String[] ansParts = answerLine.split("=", 2);
                if (ansParts.length < 2) {
                    System.out.println("⚠ Malformed file: no answer index in '" + answerLine + "'");
                    break;
                }

                int correctOption;
                try {
                    correctOption = Integer.parseInt(ansParts[1].trim());
                } catch (NumberFormatException nfe) {
                    System.out.println("⚠ Invalid answer number: " + ansParts[1]);
                    break;
                }

                if (correctOption < 1 || correctOption > options.length) {
                    System.out.println("⚠ Answer index out of range: " + correctOption);
                    break;
                }

                totalQuestions++;
                System.out.println();
                System.out.println(questionText);
                for (int i = 0; i < options.length; i++) {
                    System.out.println((i + 1) + ") " + options[i]);
                }
                System.out.print("Your answer (1-" + options.length + "): ");

                int studentAnswer;
                try {
                    studentAnswer = input.nextInt();
                    input.nextLine();
                } catch (InputMismatchException ime) {
                    input.nextLine();
                    System.out.println("⚠ Invalid input. Skipping question.");
                    continue;
                }

                if (studentAnswer == correctOption) {
                    System.out.println(" Correct!");
                    correctAnswers++;
                } else {
                    System.out.println(" Wrong! Correct answer was: " + options[correctOption - 1]);
                }
            }

            read.close();

            if (totalQuestions > 0) {
                System.out.println("\nQuiz Finished!");
                System.out.println("Total Questions Attempted: " + totalQuestions);
                System.out.println("Correct Answers:          " + correctAnswers);
                System.out.printf("Your Score:               %d%%%n", (correctAnswers * 100 / totalQuestions));
            } else {
                System.out.println("No Quiz found for course " + courseChoice + ".");
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error opening question.txt: " + e.getMessage());
        }

        saveQuizReport(totalQuestions, correctAnswers, studentId, courseChoice, quizId);
        return 0;
    }

    private boolean hasStudentAttemptedQuiz(String studentId, String quizId) {
        File file = new File("student_reports.txt");
        try {
            Scanner scanner = new Scanner(file);
            boolean foundStudent = false;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals("Student ID: " + studentId)) {
                    foundStudent = true;
                } else if (foundStudent && line.equals("Quiz ID: " + quizId)) {
                    scanner.close();
                    return true;
                } else if (line.startsWith("----------------------------------------")) {
                    foundStudent = false;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return false;
    }

    private void saveQuizReport(int totalQuestions, int correctAnswers, String studentId, int courseChoice, String quizId) {
        String courseName = getCourseName(courseChoice);
        String timestamp = java.time.LocalDateTime.now().toString();
        StringBuilder updatedContent = new StringBuilder();

        File file = new File("student_reports.txt");
        boolean studentExists = false;

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            List<String> lines = new ArrayList<>();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                updatedContent.append(line).append("\n");

                if (line.equals("Student ID: " + studentId)) {
                    studentExists = true;

                    int j = i + 1;
                    boolean skippingCourse = false;
                    String nextLine;

                    while (j < lines.size()) {
                        nextLine = lines.get(j);
                        if (nextLine.startsWith("Student ID:") || nextLine.startsWith("----------------------------------------")) {
                            break;
                        }
                        if (nextLine.startsWith("Course: " + courseName)) {
                            skippingCourse = true;
                        }
                        if (skippingCourse) {
                            j++;
                        } else {
                            updatedContent.append(nextLine).append("\n");
                            j++;
                        }
                    }

                    i = j - 1;

                    updatedContent.append("Course: ").append(courseName).append("\n");
                    updatedContent.append("Quiz ID: ").append(quizId).append("\n");
                    updatedContent.append("Date: ").append(timestamp).append("\n");
                    updatedContent.append("Attempted: ").append(totalQuestions).append("\n");
                    updatedContent.append("Correct: ").append(correctAnswers).append("\n");

                    int scorePercent = 0;
                    if (totalQuestions > 0) {
                        scorePercent = (correctAnswers * 100 / totalQuestions);
                    }
                    updatedContent.append("Score: ").append(scorePercent).append("%\n\n");
                }
            }

            if (!studentExists) {
                updatedContent.append("Student ID: ").append(studentId).append("\n");
                updatedContent.append("Course: ").append(courseName).append("\n");
                updatedContent.append("Quiz ID: ").append(quizId).append("\n");
                updatedContent.append("Date: ").append(timestamp).append("\n");
                updatedContent.append("Attempted: ").append(totalQuestions).append("\n");
                updatedContent.append("Correct: ").append(correctAnswers).append("\n");

                int scorePercent = 0;
                if (totalQuestions > 0) {
                    scorePercent = (correctAnswers * 100 / totalQuestions);
                }
                updatedContent.append("Score: ").append(scorePercent).append("%\n\n");
                updatedContent.append("----------------------------------------\n");
            }

            FileWriter writer = new FileWriter(file, false);
            writer.write(updatedContent.toString());
            writer.close();

        } catch (IOException e) {
            System.err.println("⚠ Could not update student report: " + e.getMessage());
        }
    }

    private String getCourseName(int courseNumber) {
        int count = 1;
        try {
            File file = new File("question.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.startsWith("#COURSE;")) {
                    String[] parts = line.split(";");
                    if (count == courseNumber) {
                        sc.close();
                        return (parts.length > 1 ? parts[1] : "Unknown");
                    }
                    count++;
                }
            }
            sc.close();
        } catch (Exception e) {
            return "Unknown";
        }
        return "Unknown";
    }
}
