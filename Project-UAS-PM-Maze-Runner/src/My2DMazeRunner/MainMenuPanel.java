package My2DMazeRunner;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainMenuPanel extends JPanel {

    private BufferedImage backgroundImage;
    private final JFrame window;
    private final GamePanel gamePanel;

    public MainMenuPanel(JFrame window, GamePanel gamePanel) {
        this.window = window;
        this.gamePanel = gamePanel;

        setPreferredSize(new Dimension(gamePanel.screenWidth, gamePanel.screenHeight));
        setLayout(null); // manual layout untuk posisi tombol yang sinematik
        setOpaque(false);

        loadBackground();
        createButtons();
    }

    private void loadBackground() {
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/Player/MainMenu.jpg"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            backgroundImage = null;
        }
    }

    private void createButtons() {
        int buttonWidth = 260;
        int buttonHeight = 60;
        int spacing = 25;

        int startY = (gamePanel.screenHeight / 2) - buttonHeight - spacing;
        int centerX = (gamePanel.screenWidth - buttonWidth) / 2;

        MenuButton continueButton = new MenuButton("Continue");
        MenuButton newGameButton = new MenuButton("New Game");
        MenuButton exitButton = new MenuButton("Exit");

        continueButton.setBounds(centerX, startY, buttonWidth, buttonHeight);
        newGameButton.setBounds(centerX, startY + buttonHeight + spacing, buttonWidth, buttonHeight);
        exitButton.setBounds(centerX, startY + (buttonHeight + spacing) * 2, buttonWidth, buttonHeight);

        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(continueButton);
        add(newGameButton);
        add(exitButton);
    }

    private void startGame() {
        window.getContentPane().removeAll();
        window.add(gamePanel);
        window.revalidate();
        window.repaint();

        gamePanel.requestFocusInWindow();
        gamePanel.startThread();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            // fallback warna gelap kalau gambar gagal dimuat
            g.setColor(new Color(5, 10, 20));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private static class MenuButton extends JButton {
        private boolean hover = false;

        public MenuButton(String text) {
            super(text);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setForeground(new Color(230, 230, 230));
            setFont(new Font("Cinzel", Font.BOLD, 22));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Tampilan tombol seperti contoh:
            // - Normal: kotak gelap transparan + outline oranye
            // - Hover : kotak berisi warna emas/kuning lembut
            if (hover) {
                // Isi emas saat hover
                Color top = new Color(255, 235, 150);
                Color bottom = new Color(230, 180, 60);
                GradientPaint gp = new GradientPaint(0, 0, top, 0, h, bottom);
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);
            } else {
                // Normal: background gelap sedikit transparan
                g2.setColor(new Color(40, 30, 25, 210));
                g2.fillRect(0, 0, w, h);
            }

            // Outline oranye emas
            g2.setColor(new Color(230, 170, 60));
            g2.setStroke(new BasicStroke(3f));
            g2.drawRect(1, 1, w - 3, h - 3);

            // Teks tombol
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(getText());
            int textHeight = fm.getAscent();
            int x = (w - textWidth) / 2;
            int y = (h + textHeight) / 2 - 3;

            g2.setColor(new Color(15, 15, 15, 200));
            g2.drawString(getText(), x + 1, y + 1);

            g2.setColor(new Color(235, 245, 240));
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }
}


