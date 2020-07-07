package broker;

import storage.AssetList;
import java.util.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BrokerGUI {

    public static JPanel actionsP = new JPanel();
    private static Vector<String> followV = new Vector<String>();

    public static void main(String[] args) {

        JFrame frame = new JFrame("Broker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 520);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel topP = new JPanel();
        topP.setLayout(new FlowLayout());
        topP.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JPanel centerP = new JPanel();
        centerP.setLayout(new GridLayout(14, 1, 5, 5));
        centerP.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JLabel titleL = new JLabel("[BROKER] Painel");
        topP.add(titleL);

        JLabel alert = new JLabel();
        JLabel serverL = new JLabel("Servidor:");
        JTextField serverTF = new JTextField("localhost");
        JLabel brokerL = new JLabel("Broker:");
        JTextField brokerTF = new JTextField("BKR1");
        JLabel topicL = new JLabel("Ativo:");
        Vector<String> assets = AssetList.load();
        JComboBox<String> assetsCB = new JComboBox<String>(assets);
        JLabel qtdeL = new JLabel("Quantidade:");
        JTextField qtdeTF = new JTextField();
        JLabel priceL = new JLabel("Preço:");
        JTextField priceTF = new JTextField();
        JButton buyBtn = new JButton("Comprar");
        JButton saleBtn = new JButton("Vender");
        JPanel groupBtn = new JPanel();
        groupBtn.setLayout(new GridLayout(1, 2, 5, 5));
        groupBtn.add(buyBtn);
        groupBtn.add(saleBtn);
        JButton viewBtn = new JButton("Abrir Visualizador");
        centerP.add(alert);
        centerP.add(serverL);
        centerP.add(serverTF);
        centerP.add(brokerL);
        centerP.add(brokerTF);
        centerP.add(topicL);
        centerP.add(assetsCB);
        centerP.add(qtdeL);
        centerP.add(qtdeTF);
        centerP.add(priceL);
        centerP.add(priceTF);
        centerP.add(groupBtn);
        centerP.add(viewBtn);

        frame.add(topP, BorderLayout.NORTH);
        frame.add(centerP, BorderLayout.CENTER);
        frame.setVisible(true);

        brokerTF.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (brokerTF.getText().length() < 4
                        && (Character.isAlphabetic(ke.getKeyChar()) || ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9')
                        || ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    brokerTF.setEditable(true);
                    alert.setText(brokerTF.getText().length() == 3 ? "" : "'Broker' deve possuir 4 caracteres.");
                } else {
                    brokerTF.setEditable(false);
                    alert.setText("* O tamanho máximo para 'Broker' é de 4 caracteres.");
                }
            }
        });
        qtdeTF.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' || ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    qtdeTF.setEditable(true);
                    alert.setText("");
                } else {
                    qtdeTF.setEditable(false);
                    alert.setText("* Digite apenas números (0-9)");
                }
            }
        });
        priceTF.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' || ke.getKeyCode() == KeyEvent.VK_BACK_SPACE
                        || ke.getKeyChar() == '.' && priceTF.getText().length() > 0
                                && !priceTF.getText().contains(".")) {
                    priceTF.setEditable(true);
                    alert.setText("");
                } else {
                    priceTF.setEditable(false);
                    alert.setText("* Digite apenas números (0-9) e um ponto (.) para decimais.");
                }
            }
        });
        buyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (brokerTF.getText().length() == 4 && qtdeTF.getText().length() > 0 && priceTF.getText().length() > 0
                        && serverTF.getText().length() > 0) {
                    String host = serverTF.getText();
                    String topic = "compra." + assetsCB.getSelectedItem().toString().toLowerCase();
                    String message = "<" + qtdeTF.getText() + ";" + priceTF.getText() + ";"
                            + brokerTF.getText().toUpperCase() + ">";

                    BrokerEmit bEmit = new BrokerEmit(host, topic, message);
                    bEmit.start();

                    qtdeTF.setText("");
                    priceTF.setText("");
                }
            }
        });
        saleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (brokerTF.getText().length() == 4 && qtdeTF.getText().length() > 0 && priceTF.getText().length() > 0
                        && serverTF.getText().length() > 0) {
                    String host = serverTF.getText();
                    String topic = "venda." + assetsCB.getSelectedItem().toString().toLowerCase();
                    String message = "<" + qtdeTF.getText() + ";" + priceTF.getText() + ";"
                            + brokerTF.getText().toUpperCase() + ">";

                    BrokerEmit bEmit = new BrokerEmit(host, topic, message);
                    bEmit.start();

                    qtdeTF.setText("");
                    priceTF.setText("");
                }
            }
        });
        viewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewer();
            }
        });
    }

    private static void viewer() {

        JFrame frame = new JFrame("Broker");
        frame.setSize(320, 600);
        frame.setLocation(new Point(100, 60));
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        JPanel topP = new JPanel();
        topP.setLayout(new FlowLayout());
        JPanel centerP = new JPanel();
        centerP.setLayout(new BoxLayout(centerP, BoxLayout.Y_AXIS));
        centerP.setBorder(BorderFactory.createEmptyBorder(0, 10, 9, 10));

        JLabel titleL = new JLabel("[BROKER] Viewer");
        topP.add(titleL);

        JPanel content = new JPanel();
        content.setLayout(new GridLayout(7, 1, 5, 5));

        JLabel serverL = new JLabel("Servidor:");
        JTextField serverTF = new JTextField("localhost");
        JLabel topicL = new JLabel("Ativo:");
        Vector<String> assets = AssetList.load();
        JComboBox<String> assetsCB = new JComboBox<String>(assets);
        JButton addBtn = new JButton("Acompanhar");
        JLabel followL = new JLabel("Acompanhando:");
        JLabel followListL = new JLabel();
        actionsP.setLayout(new BoxLayout(actionsP, BoxLayout.Y_AXIS));
        JScrollPane scrollP = new JScrollPane(actionsP);
        scrollP.setPreferredSize(new Dimension(200, 300));
        followListL.setText(followV.size() > 0 ? followV.toString() : "...");

        content.add(serverL);
        content.add(serverTF);
        content.add(topicL);
        content.add(assetsCB);
        content.add(addBtn);
        content.add(followL);
        content.add(followListL);

        centerP.add(content);
        centerP.add(scrollP);

        frame.add(topP, BorderLayout.NORTH);
        frame.add(centerP, BorderLayout.CENTER);
        frame.setVisible(true);

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!followV.contains(assetsCB.getSelectedItem().toString())) {
                    followV.add(assetsCB.getSelectedItem().toString());
                    followListL.setText(followV.toString());
                    BrokerReceive bReceive = new BrokerReceive(serverTF.getText(),
                            "*." + followV.get(followV.size() - 1).toLowerCase());
                    bReceive.start();
                }
            }
        });
    }
}