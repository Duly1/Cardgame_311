package com.example.csc311_24game;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HelloController {

    @FXML private ImageView card1, card2, card3, card4;
    @FXML private TextField expressionField;
    @FXML private Button findSolutionButton, refreshButton, verifyButton;

    private int[] currentCardValues = new int[4];
    private final Random random = new Random();

    @FXML
    public void initialize() {
        generateRandomCards();
        refreshButton.setOnAction(e -> generateRandomCards());
        verifyButton.setOnAction(e -> verifyExpression());
    }

    private void generateRandomCards() {
        List<Card> deck = generateDeck();
        Collections.shuffle(deck);
        Card[] selectedCards = deck.subList(0, 4).toArray(new Card[0]);

        card1.setImage(new Image(getClass().getResourceAsStream(selectedCards[0].getImageFile())));
        card2.setImage(new Image(getClass().getResourceAsStream(selectedCards[1].getImageFile())));
        card3.setImage(new Image(getClass().getResourceAsStream(selectedCards[2].getImageFile())));
        card4.setImage(new Image(getClass().getResourceAsStream(selectedCards[3].getImageFile())));

        for (int i = 0; i < 4; i++) currentCardValues[i] = selectedCards[i].getValue();
    }

    private List<Card> generateDeck() {
        List<Card> deck = new ArrayList<>();
        String[] suits = {"clubs", "diamonds", "hearts", "spades"};
        String[] faces = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 1};

        for (String suit : suits) {
            for (int i = 0; i < faces.length; i++) {
                String imagePath = "/com/example/csc311_24game/images/" + faces[i] + "_of_" + suit + ".png";
                deck.add(new Card(values[i], imagePath));
            }
        }
        return deck;
    }

    private void verifyExpression() {
        String expression = expressionField.getText().trim();
        if (expression.isEmpty()) {
            showAlert("Error", "Please enter an expression!");
            return;
        }

        Pattern numberPattern = Pattern.compile("\\d+");
        Matcher matcher = numberPattern.matcher(expression);
        List<Integer> numbersUsed = new ArrayList<>();
        while (matcher.find()) numbersUsed.add(Integer.parseInt(matcher.group()));

        Arrays.sort(currentCardValues);
        Collections.sort(numbersUsed);

        List<Integer> cardList = Arrays.stream(currentCardValues).boxed().collect(Collectors.toList());
        if (!numbersUsed.equals(cardList)) {
            showAlert("Error", "The numbers used in the expression do not match the cards shown!");
            return;
        }

        try {
            ExpressionEvaluator evaluator = new ExpressionEvaluator(expression);
            double result = evaluator.parse();
            if (Math.abs(result - 24) < 0.0001)
                showAlert("Success", "Congratulations! Your expression evaluates to 24!");
            else
                showAlert("Incorrect", "Your expression evaluates to " + result + ", not 24.");
        } catch (Exception e) {
            showAlert("Error", "Invalid expression! Please check your input.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    static class Card {
        private final int value;
        private final String imageFile;
        public Card(int value, String imageFile) { this.value = value; this.imageFile = imageFile; }
        public int getValue() { return value; }
        public String getImageFile() { return imageFile; }
    }
}
