import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.ImageIcon;

public class QuizApp {

    private static final Color APP_BG = new Color(80, 150, 220);
    private static final Color CARD_BG = new Color(109, 123, 215);
    private static final Color PRIMARY = new Color(25, 118, 210);
    private static final Color PRIMARY_DARK = new Color(255, 255, 255);
    private static final Color MUTED_TEXT = new Color(236, 236, 0);
    private static final int TIME_PER_QUESTION_SECONDS = 20;

    static class Question {
        String question;
        String correctAnswer;
        List<String> options;

        Question(String question, String correctAnswer, List<String> options) {
            this.question = question;
            this.correctAnswer = correctAnswer;
            this.options = options;
        }
    }

    static class Stage {
        String name;
        int points;
        List<Question> questions;

        Stage(String name, int points, List<Question> questions) {
            this.name = name;
            this.points = points;
            this.questions = questions;
        }
    }

    private final List<Stage> stages = new ArrayList<>();
    private int currentStageIndex = 0;
    private int currentQuestionIndex = 0;
    private int totalScore = 0;
    private int[] stageScores;

    private final JFrame frame;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    private JLabel quizStageLabel;
    private JLabel quizQuestionCountLabel;
    private JTextArea quizQuestionText;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionGroup;
    private JButton nextButton;
    private JProgressBar progressBar;
    private JLabel timerLabel;
    private JProgressBar timerBar;
    private Timer questionTimer;
    private int timeRemaining;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizApp::new);
        // ImageIcon image= new ImageIcon("logo");
        //        frame.setIconImage(image.getImage());

    }

    public QuizApp() {
        setSystemLookAndFeel();
        initializeStages();

        frame = new JFrame("3-Stage Aptitude Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(860, 620);
        frame.setMinimumSize(new Dimension(780, 560));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(APP_BG);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(APP_BG);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        cardPanel.add(createWelcomePanel(), "welcome");
        cardPanel.add(createRulesPanel(), "rules");
        cardPanel.add(createQuizPanel(), "quiz");

        frame.setContentPane(cardPanel);
        cardLayout.show(cardPanel, "welcome");
        frame.setVisible(true);
    }

    private void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    private void initializeStages() {
        List<Question> stage1 = new ArrayList<>(getLevel1Questions());
        List<Question> stage2 = new ArrayList<>(getLevel2Questions());
        List<Question> stage3 = new ArrayList<>(getLevel3Questions());

        Collections.shuffle(stage1);
        Collections.shuffle(stage2);
        Collections.shuffle(stage3);

        stages.add(new Stage("STAGE 1: BASIC MATH", 1, stage1));
        stages.add(new Stage("STAGE 2: LOGICAL REASONING", 2, stage2));
        stages.add(new Stage("STAGE 3: ABSTRACT THINKING", 3, stage3));
        stageScores = new int[stages.size()];
    }

    private Font font(int style, int size) {
        return new Font("Segoe UI", style, size);
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(Color.CYAN);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 223, 233)),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));
        return card;
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(APP_BG);

        JPanel card = createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("WELCOME TO 3-STAGE APTITUDE TEST", SwingConstants.CENTER);
        title.setFont(font(Font.BOLD, 29));
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        title.setForeground(PRIMARY_DARK);

        JLabel subtitle = new JLabel("Test your math, logic, and abstract reasoning", SwingConstants.CENTER);
        subtitle.setFont(font(Font.PLAIN, 17));
        subtitle.setForeground(MUTED_TEXT);
        subtitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JButton startButton = new JButton("Start");
        startButton.setFont(font(Font.BOLD, 18));
        startButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        startButton.setPreferredSize(new Dimension(180, 44));
        startButton.setMaximumSize(new Dimension(220, 50));
        stylePrimaryButton(startButton);
        startButton.addActionListener(e -> cardLayout.show(cardPanel, "rules"));

        card.add(Box.createVerticalGlue());
        card.add(title);
        card.add(Box.createVerticalStrut(14));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(32));
        card.add(startButton);
        card.add(Box.createVerticalGlue());

        panel.add(card, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRulesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(APP_BG);

        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 14));

        JLabel title = new JLabel("Rules", SwingConstants.CENTER);
        title.setFont(font(Font.BOLD, 28));
        title.setForeground(PRIMARY_DARK);

        JTextArea rulesText = new JTextArea(
                "                                     - The test consists of 3 stages.\n"
                        + "                                     - Each stage has 5 multiple-choice questions.\n"
                        + "                                     - Each question has 4 options.\n"
                        + "                                     - Stage 1 gives 1 point per correct answer.\n"
                        + "                                     - Stage 2 gives 2 points per correct answer.\n"
                        + "                                     - Stage 3 gives 3 points per correct answer.\n"
                        + "                                     - Maximum possible score is 30 points.\n"
                        + "                                     - Questions and options are shuffled each run.\n"
                        + "                                     - You have " + TIME_PER_QUESTION_SECONDS + " seconds per question.");
        rulesText.setFont(font(Font.BOLD, 17));
        rulesText.setEditable(false);
        rulesText.setOpaque(false);
        rulesText.setLineWrap(true);
        rulesText.setWrapStyleWord(true);
        rulesText.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        rulesText.setForeground(new Color(255, 255, 255));

        JButton beginButton = new JButton("Begin Test");
        beginButton.setFont(font(Font.BOLD, 17));
        stylePrimaryButton(beginButton);
        beginButton.addActionListener(e -> {
            currentStageIndex = 0;
            currentQuestionIndex = 0;
            totalScore = 0;
            stageScores = new int[stages.size()];
            showCurrentQuestion();
            cardLayout.show(cardPanel, "quiz");
        });

        JPanel bottom = new JPanel();
        bottom.add(beginButton);

        bottom.setBackground(CARD_BG);

        card.add(title, BorderLayout.NORTH);
        card.add(rulesText, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);
        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createQuizPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(APP_BG);

        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(12, 10));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBackground(CARD_BG);

        quizStageLabel = new JLabel("Stage");
        quizStageLabel.setFont(font(Font.BOLD, 22));
        quizStageLabel.setForeground(PRIMARY_DARK);

        quizQuestionCountLabel = new JLabel("Question");
        quizQuestionCountLabel.setFont(font(Font.PLAIN, 15));
        quizQuestionCountLabel.setForeground(MUTED_TEXT);

        timerLabel = new JLabel("Time Left: " + TIME_PER_QUESTION_SECONDS + "s");
        timerLabel.setFont(font(Font.BOLD, 15));
        timerLabel.setForeground(new Color(255, 0, 60));

        timerBar = new JProgressBar(0, TIME_PER_QUESTION_SECONDS);
        timerBar.setValue(TIME_PER_QUESTION_SECONDS);
        timerBar.setForeground(new Color(123, 81, 111));
        timerBar.setStringPainted(false);

        progressBar = new JProgressBar(0, 15);
        progressBar.setForeground(PRIMARY);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);

        top.add(quizStageLabel);
        top.add(Box.createVerticalStrut(4));
        top.add(quizQuestionCountLabel);
        top.add(Box.createVerticalStrut(6));
        top.add(timerLabel);
        top.add(Box.createVerticalStrut(6));
        top.add(timerBar);
        top.add(Box.createVerticalStrut(10));
        top.add(progressBar);

        quizQuestionText = new JTextArea();
        quizQuestionText.setLineWrap(true);
        quizQuestionText.setWrapStyleWord(true);
        quizQuestionText.setEditable(false);
        quizQuestionText.setFont(font(Font.PLAIN, 18));
        quizQuestionText.setBackground(new Color(109, 123, 215));
        quizQuestionText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230)),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        optionButtons = new JRadioButton[4];
        optionGroup = new ButtonGroup();

        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(font(Font.PLAIN, 16));
            optionButtons[i].setBackground(CARD_BG);
            optionButtons[i].setActionCommand(String.valueOf(i));
            optionGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        optionsPanel.setBackground(CARD_BG);

        JScrollPane questionScroll = new JScrollPane(quizQuestionText);
        questionScroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel center = new JPanel(new BorderLayout(10, 14));
        center.setBackground(CARD_BG);
        center.add(questionScroll, BorderLayout.NORTH);
        center.add(optionsPanel, BorderLayout.CENTER);

        nextButton = new JButton("Next");
        nextButton.setFont(font(Font.BOLD, 16));
        stylePrimaryButton(nextButton);
        nextButton.addActionListener(e -> submitCurrentAnswer());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(CARD_BG);
        bottom.add(nextButton, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);
        panel.add(card, BorderLayout.CENTER);

        return panel;
    }

    private void startQuestionTimer() {
        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop();
        }

        timeRemaining = TIME_PER_QUESTION_SECONDS;
        updateTimerUI();

        questionTimer = new Timer(1000, e -> {
            timeRemaining--;
            updateTimerUI();
            if (timeRemaining <= 0) {
                questionTimer.stop();
                handleTimeUp();
            }
        });
        questionTimer.start();
    }

    private void updateTimerUI() {
        timerLabel.setText("Time Left: " + timeRemaining + "s");
        timerBar.setValue(timeRemaining);
    }

    private void handleTimeUp() {
        Stage stage = stages.get(currentStageIndex);
        Question question = stage.questions.get(currentQuestionIndex);
        JOptionPane.showMessageDialog(frame,
                "Time is up. Correct answer: " + question.correctAnswer,
                "Time Up",
                JOptionPane.INFORMATION_MESSAGE);
        moveToNextQuestion();
    }

    private void showCurrentQuestion() {
        Stage stage = stages.get(currentStageIndex);
        Question question = stage.questions.get(currentQuestionIndex);

        quizStageLabel.setText(stage.name + " (" + stage.points + " point" + (stage.points > 1 ? "s" : "") + " each)");
        quizQuestionCountLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + stage.questions.size());
        quizQuestionText.setText(question.question);

        List<String> shuffledOptions = new ArrayList<>(question.options);
        Collections.shuffle(shuffledOptions);
        question.options = shuffledOptions;

        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText((char) ('A' + i) + ") " + question.options.get(i));
            optionButtons[i].setSelected(false);
        }

        optionGroup.clearSelection();

        int globalQuestion = (currentStageIndex * 5) + currentQuestionIndex;
        progressBar.setValue(globalQuestion);
        progressBar.setString((globalQuestion) + " / 15 answered");

        boolean isLastQuestion = currentStageIndex == stages.size() - 1
                && currentQuestionIndex == stage.questions.size() - 1;
        nextButton.setText(isLastQuestion ? "Finish" : "Next");

        startQuestionTimer();
    }

    private void submitCurrentAnswer() {
        if (optionGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(frame, "Please choose an answer before continuing.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop();
        }

        Stage stage = stages.get(currentStageIndex);
        Question question = stage.questions.get(currentQuestionIndex);
        int selectedIndex = Integer.parseInt(optionGroup.getSelection().getActionCommand());
        String selectedAnswer = question.options.get(selectedIndex);

        if (selectedAnswer.equals(question.correctAnswer)) {
            totalScore += stage.points;
            stageScores[currentStageIndex] += stage.points;
            JOptionPane.showMessageDialog(frame,
                    "Correct! +" + stage.points + " point" + (stage.points > 1 ? "s" : "") + ".");
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Wrong answer. Correct answer: " + question.correctAnswer,
                    "Result",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        moveToNextQuestion();
    }

    private void moveToNextQuestion() {
        Stage stage = stages.get(currentStageIndex);

        currentQuestionIndex++;
        if (currentQuestionIndex >= stage.questions.size()) {
            currentStageIndex++;
            currentQuestionIndex = 0;
        }

        if (currentStageIndex >= stages.size()) {
            showFinalResult();
        } else {
            showCurrentQuestion();
        }
    }

    private void showFinalResult() {
        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop();
        }

        progressBar.setValue(15);
        progressBar.setString("15 / 15 answered");
        timerLabel.setText("Time Left: 0s");
        timerBar.setValue(0);

        String performance;
        if (totalScore < 10) {
            performance = "Needs Improvement. Keep practicing!";
        } else if (totalScore < 20) {
            performance = "Average. You can do better!";
        } else if (totalScore < 30) {
            performance = "Great Job! Well done!";
        } else {
            performance = "Perfect Score! Outstanding!";
        }

        String message = "Final Score: " + totalScore + " / 30\n\n"
                + "Stage 1 Score: " + stageScores[0] + " / 5\n"
                + "Stage 2 Score: " + stageScores[1] + " / 10\n"
                + "Stage 3 Score: " + stageScores[2] + " / 15\n\n"
                + "Performance: " + performance;
        JOptionPane.showMessageDialog(frame, message, "Quiz Completed", JOptionPane.INFORMATION_MESSAGE);

        int option = JOptionPane.showConfirmDialog(frame, "Would you like to restart the quiz?", "Restart",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            stages.clear();
            initializeStages();
            currentStageIndex = 0;
            currentQuestionIndex = 0;
            totalScore = 0;
            stageScores = new int[stages.size()];
            showCurrentQuestion();
        } else {
            frame.dispose();
        }
    }

    // LEVEL 1
    private static List<Question> getLevel1Questions() {
        List<Question> list = new ArrayList<>();

        list.add(new Question("Solve for x: 3x + 12 = 45.", "11", Arrays.asList("9", "11", "13", "15")));
        list.add(new Question("A jacket is on sale for 20% off. If the sale price is $80, what was the original price?",
                "$100", Arrays.asList("$90", "$96", "$100", "$120")));
        list.add(new Question(
                "A train travels at 60 km/h for 2 hours and then 90 km/h for the next 3 hours. What is the average speed for the entire journey?",
                "78 km/h", Arrays.asList("75 km/h", "78 km/h", "80 km/h", "82 km/h")));
        list.add(new Question(
                "If 5 machines can produce 5 widgets in 5 minutes, how many minutes does it take 100 machines to produce 100 widgets?",
                "5 minutes", Arrays.asList("1 minute", "5 minutes", "20 minutes", "100 minutes")));
        list.add(new Question(
                "A bat and a ball cost $1.10 in total. The bat costs $1.00 more than the ball. How much does the ball cost?",
                "$0.05", Arrays.asList("$0.05", "$0.10", "$0.15", "$1.00")));

        return list;
    }

    // LEVEL 2
    private static List<Question> getLevel2Questions() {
        List<Question> list = new ArrayList<>();

        list.add(new Question("Complete the sequence: 2, 6, 12, 20, 30, ?", "42",
                Arrays.asList("36", "40", "42", "48")));
        list.add(new Question("If WATER is written as XBUFS, how is BLOOD written in that same code?", "CMPPE",
                Arrays.asList("AMNNC", "CMPPE", "CPPPE", "CNPPE")));
        list.add(new Question(
                "Pointing to a photograph, a man says, 'I have no brother or sister, but that man’s father is my father’s son.' Who is in the photograph?",
                "His son", Arrays.asList("His father", "His nephew", "His son", "Himself")));
        list.add(new Question(
                "In a race of 5 people (A, B, C, D, E): A finished ahead of B but behind C. D finished ahead of E but behind B. Who finished last?",
                "E", Arrays.asList("B", "C", "D", "E")));
        list.add(new Question(
                "In a group of 15 people, 7 read French, 8 read English, and 3 read both. How many people in the group read neither French nor English?",
                "3", Arrays.asList("2", "3", "4", "5")));

        return list;
    }

    // LEVEL 3
    private static List<Question> getLevel3Questions() {
        List<Question> list = new ArrayList<>();

        list.add(new Question("Find the odd one out among the following units of measurement.", "Ounce",
                Arrays.asList("Inch", "Ounce", "Centimeter", "Yard")));
        list.add(new Question(
                "If a clock shows exactly 3:00, what is the interior angle between the hour hand and the minute hand?",
                "90°", Arrays.asList("45°", "60°", "90°", "120°")));
        list.add(new Question(
                "A pattern follows this rule: The number of sides in the shape increases by one, and the number of dots inside the shape doubles. If the first shape is a triangle with 2 dots, what is the fourth shape?",
                "Hexagon with 16 dots", Arrays.asList("Pentagon with 8 dots", "Hexagon with 8 dots",
                "Pentagon with 16 dots", "Hexagon with 16 dots")));
        list.add(new Question(
                "A 3 x 3 x 3 cube is painted red on all outside faces. How many of the 1-unit small cubes have exactly two sides painted?",
                "12", Arrays.asList("8", "12", "14", "18")));
        list.add(new Question(
                "You are given a sequence of shapes: 1. A single line, 2. A 'V' shape, 3. A triangle, 4. A square. What is the next logical shape, and how many internal diagonals does it have?",
                "Pentagon; 5 diagonals", Arrays.asList("Pentagon; 2 diagonals", "Hexagon; 5 diagonals",
                "Pentagon; 5 diagonals", "Hexagon; 9 diagonals")));

        return list;
    }
}
