package javachess;

import javax.swing.*;
import java.awt.*;

public class CaseLabel extends JLabel {
    private static final Color DEFAULT_COLOR = Color.GRAY;
    private boolean showCircle;
    private Color color = DEFAULT_COLOR;
    private String upperLeftCornerLabel = "";
    private String lowerRightCornerLabel = "";

    public void setDrawCircle(boolean showCircle) {
        this.showCircle = showCircle;
        repaint();
    }

    public void setDrawCircle(boolean showCircle, Color color) {
        this.color = color;
        setDrawCircle(showCircle);
    }

    public void setUpperLeftCornerLabel(String label) {
        this.upperLeftCornerLabel = label;
        repaint();
    }

    public void setLowerRightCornerLabel(String label) {
        this.lowerRightCornerLabel = label;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw circle if enabled
        if (showCircle) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 128));

            float thickness = 8.0f;
            g2d.setStroke(new BasicStroke(thickness));

            int diameter = (int) (Math.min(getWidth(), getHeight()) * 0.9 - thickness);
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;
            g2d.drawOval(x, y, diameter, diameter);

            g2d.setColor(DEFAULT_COLOR);
        }

        if (!lowerRightCornerLabel.isEmpty()) {
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            FontMetrics metrics = g.getFontMetrics();
            int labelWidth = metrics.stringWidth(lowerRightCornerLabel);

            int x = getWidth() - labelWidth - 5;
            int y = getHeight() - 5;
            g.setColor(Color.GRAY);
            g.drawString(lowerRightCornerLabel, x, y);
        }

        if (!upperLeftCornerLabel.isEmpty()) {
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            FontMetrics metrics = g.getFontMetrics();
            int labelHeight = metrics.getHeight();

            int x = 5;
            int y = labelHeight + 5;
            g.setColor(Color.GRAY);
            g.drawString(upperLeftCornerLabel, x, y);
        }
    }
}