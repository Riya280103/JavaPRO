import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleCalculatorGUI extends JFrame implements ActionListener {

    private JPanel panel;
    private JTextField displayField;
    private JButton[] numberButtons;
    private JButton[] operationButtons;
    private JButton equalsButton;
    private JButton clearButton;

    private double num1, num2, result;
    private char operation;

    public SimpleCalculatorGUI() {

        setTitle("Simple Calculator");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 3, 10, 10));

        displayField = new JTextField();
        displayField.setEditable(false);
        displayField.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(displayField);

        numberButtons = new JButton[10];
        for (int i = 0; i < numberButtons.length; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            panel.add(numberButtons[i]);
        }

        operationButtons = new JButton[4];
        operationButtons[0] = new JButton("+");
        operationButtons[1] = new JButton("-");
        operationButtons[2] = new JButton("*");
        operationButtons[3] = new JButton("/");
        for (int i = 0; i < operationButtons.length; i++) {
            operationButtons[i].addActionListener(this);
            panel.add(operationButtons[i]);
        }

        equalsButton = new JButton("=");
        equalsButton.addActionListener(this);
        panel.add(equalsButton);

        clearButton = new JButton("C");
        clearButton.addActionListener(this);
        panel.add(clearButton);

        add(panel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // If a number button is clicked
        for (int i = 0; i < numberButtons.length; i++) {
            if (command.equals(String.valueOf(i))) {
                displayField.setText(displayField.getText() + i);
                return;
            }
        }

        switch (command) {
            case "+":
            case "-":
            case "*":
            case "/":
                num1 = Double.parseDouble(displayField.getText());
                operation = command.charAt(0);
                displayField.setText("");
                break;

            case "=":
                num2 = Double.parseDouble(displayField.getText());
                switch (operation) {
                    case '+':
                        result = num1 + num2;
                        break;
                    case '-':
                        result = num1 - num2;
                        break;
                    case '*':
                        result = num1 * num2;
                        break;
                    case '/':
                        if (num2 == 0) {
                            displayField.setText("Error");
                        } else {
                            result = num1 / num2;
                        }
                        break;
                }
                displayField.setText(String.valueOf(result));
                break;

            case "C":
                displayField.setText("");
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SimpleCalculatorGUI();
            }
        });
    }
}
