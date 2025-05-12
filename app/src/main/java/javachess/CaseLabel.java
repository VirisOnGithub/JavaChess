package javachess;

import javax.swing.*;
import java.awt.*;

/**
 * Custom JLabel representing a case that is able to draw circles depending on where the pieces can go.
 * It also displays label showing coordinates
 */
public class CaseLabel extends JLabel {
    private static final Color DEFAULT_COLOR = Color.GRAY;
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 20);
    private static final float CIRCLE_THICKNESS = 8.0f;
    private static final int PADDING = 5;

    private boolean showCircle;
    private Color circleColor = DEFAULT_COLOR;
    private String upperLeftCornerLabel = "";
    private String lowerRightCornerLabel = "";

    public void setDrawCircle(boolean showCircle) {
        this.showCircle = showCircle;
        repaint();
    }

    public void setDrawCircle(boolean showCircle, Color color) {
        this.circleColor = color;
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
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smoother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw circle if enabled
        if (showCircle) {
            drawCircle(g2d);
        }

        // Draw labels
        g.setFont(LABEL_FONT);
        g.setColor(DEFAULT_COLOR);

        if (!upperLeftCornerLabel.isEmpty()) {
            drawLabel(g, upperLeftCornerLabel, PADDING, PADDING + g.getFontMetrics().getAscent());
        }

        if (!lowerRightCornerLabel.isEmpty()) {
            FontMetrics metrics = g.getFontMetrics();
            int labelWidth = metrics.stringWidth(lowerRightCornerLabel);
            int x = getWidth() - labelWidth - PADDING;
            int y = getHeight() - PADDING;
            drawLabel(g, lowerRightCornerLabel, x, y);
        }
    }

    private void drawCircle(Graphics2D g2d) {
        g2d.setColor(new Color(circleColor.getRed(), circleColor.getGreen(), circleColor.getBlue(), 128));
        g2d.setStroke(new BasicStroke(CIRCLE_THICKNESS));

        int diameter = (int) (Math.min(getWidth(), getHeight()) * 0.9 - CIRCLE_THICKNESS);
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;
        g2d.drawOval(x, y, diameter, diameter);
    }

    private void drawLabel(Graphics g, String label, int x, int y) {
        g.drawString(label, x, y);
    }
}