import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AuthGui extends JFrame {
    //поля для входа
    private JTextField login;
    private JPasswordField password;
    private JLabel logMessage;


    // поля для регистрации

    private JTextField nameForReg;
    private JPasswordField passwordForReg;
    private JTextField loginForReg;
    private JLabel message_reg;


    private final Connection connection;
    private boolean isConnected;

    public AuthGui() {
        MainData dataBase = new MainData("jdbc:mysql://localhost:3306/chat_users", "root", "root");
        connection = dataBase.getConnection();
        prepareGui();

    }

    private void prepareGui() {
        setBounds(100, 100, 500, 200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel greet = new JLabel("Welcome to chat. Login or register.");
        greet.setFont(new Font("Verdana", Font.ITALIC, 18));
        add(greet, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(0, 2));

        JPanel logPanel = new JPanel(new GridLayout(3, 1));
        logPanel.setBackground(new Color(180, 208, 210));

        JLabel loginLabel = new JLabel("Login");
        login = new JTextField();
        JLabel passwordLabel = new JLabel("Password");
        password = new JPasswordField();

        logMessage = new JLabel();

        JButton checkLogin = new JButton("Submit");
        checkLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE login = \'" + login.getText() + "\'AND password=\'"
                            + password.getText() + "\'");
                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(null, "You are welcome, " + resultSet.getString("name"));
                        isConnected = true;
                    } else {
                        logMessage.setForeground(Color.RED);
                        password.setText(null);
                        login.setText(null);
                        logMessage.setText("User no found");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        logPanel.add(loginLabel);
        logPanel.add(login);
        logPanel.add(passwordLabel);
        logPanel.add(password);
        logPanel.add(logMessage);
        logPanel.add(checkLogin);

        JPanel regPanel = new JPanel(new GridLayout(0, 2));
        regPanel.setBackground(new Color(114, 160, 165));
        JLabel nameLabel = new JLabel("Name");
        nameForReg = new JTextField();
        JLabel regLoginLabel = new JLabel("Login");
        loginForReg = new JTextField();
        JLabel regPasswordLabel = new JLabel("Password");
        passwordForReg = new JPasswordField();
        message_reg = new JLabel();
        JButton registration = new JButton("Reg");

        registration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE login = \'" + loginForReg.getText() + "\'");
                    if (!resultSet.next()) {
                        connection.setAutoCommit(false);
                        PreparedStatement preStatement = connection.prepareStatement("INSERT INTO users (name, login, password) VALUE (?, ?,?)");
                        preStatement.setString(1, nameForReg.getText());
                        preStatement.setString(2, loginForReg.getText());
                        preStatement.setString(3, passwordForReg.getText());
                        preStatement.execute(); // запись execute для select , executeQuery для select или delete
                        connection.commit();
                        isConnected = true;

                    } else {
                        message_reg.setForeground(Color.RED);
                        message_reg.setText("login is occupied");
                        loginForReg.setText(null);
                    }

                } catch (SQLException throwables) {
                    try {
                        connection.rollback(); // если не ок, то роллбек и отменяем что наделали, откатываемся назад
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    throwables.printStackTrace();
                    throw new RuntimeException("Statement error");
                }

            }


        });

        regPanel.add(nameLabel);
        regPanel.add(nameForReg);
        regPanel.add(regLoginLabel);
        regPanel.add(loginForReg);
        regPanel.add(regPasswordLabel);
        regPanel.add(passwordForReg);
        regPanel.add(message_reg);
        regPanel.add(registration);

        mainPanel.add(logPanel);
        mainPanel.add(regPanel);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
        while (!isConnected)
            Thread.onSpinWait();
        dispose();

        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}






