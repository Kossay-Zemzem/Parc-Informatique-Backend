package com.project.parc;

import jakarta.annotation.PreDestroy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.awt.*;
import java.net.InetAddress;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class ParcApplication {
    private static final Logger sessionLogger = LogManager.getLogger("com.project.ProjectYC");
    private static final String SESSION_ID = generateSessionId();


    public static void main(String[] args) {
        // Make sure AWT GUI APIs are allowed
        System.setProperty("java.awt.headless", "false"); //IMPORTANT FOR TRAY ICON AND DEKSTOP LAUNCHE TO WORK

        // Set the session ID as a system property so Log4j can use it
        System.setProperty("sessionId", SESSION_ID);
        sessionLogger.info("=== APPLICATION STARTUP ===");
        sessionLogger.info("Session ID: {}", System.getProperty("sessionId"));

        //SpringApplication.run(ParcApplication.class, args);
        // Tray icon setup
        ConfigurableApplicationContext ctx = SpringApplication.run(ParcApplication.class, args);

        // Get port from application.properties
        Environment env = ctx.getEnvironment();
        String port = env.getProperty("server.port", "8080");

        try {
            // Prefer server.address if explicitly set
            String hostAddress = env.getProperty("server.address");
            if (hostAddress == null || hostAddress.isBlank()) {
                hostAddress = InetAddress.getLocalHost().getHostAddress(); // Else, Get current host machine's IP address
            }

            String url = "http://" + hostAddress + ":" + port + "/home";
            System.out.println("App running at: " + url);
            //Auto-launch in default browser
            if (Boolean.parseBoolean(env.getProperty("app.autoLaunchBrowser", "true"))) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI(url));
                }
            }

            // Start tray with correct URL
            String finalUrl = url;
            new Thread(() -> addTrayIcon(finalUrl)).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTrayIcon(String url) {
        try {
            if (!SystemTray.isSupported()) {
                System.out.println("System tray not supported");
                return;
            }

            SystemTray tray = SystemTray.getSystemTray();
//			Image image = Toolkit.getDefaultToolkit().createImage("./images/icon.png");
            Image image = Toolkit.getDefaultToolkit().getImage(
                    ParcApplication.class.getResource("/images/tray32V4.png")
            );

            PopupMenu menu = new PopupMenu();
            Font biggerFont = new Font("Dialog", Font.PLAIN, 13); // default is ~12


            menu.setFont(biggerFont);
            // Open UI
            MenuItem openUI = new MenuItem("Open App");
            openUI.addActionListener(e -> {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            openUI.setFont(biggerFont);
            menu.add(openUI);

            // Exit
            MenuItem exit = new MenuItem("Exit");
            exit.addActionListener(e -> {
                System.exit(0);
            });
            exit.setFont(biggerFont);
            menu.add(exit);

            TrayIcon trayIcon = new TrayIcon(image, "Parc Informatique", menu);
            trayIcon.setImageAutoSize(true);

            // Add click listener to the tray icon
            trayIcon.addActionListener(e -> {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            tray.add(trayIcon);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void onShutdown() {
        sessionLogger.info("=== APPLICATION SHUTDOWN ===");
    }

    private static String generateSessionId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return LocalDateTime.now().format(formatter);
    }
}
