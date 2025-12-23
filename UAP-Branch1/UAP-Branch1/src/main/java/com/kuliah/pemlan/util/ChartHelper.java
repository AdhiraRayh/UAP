package com.kuliah.pemlan.util;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ChartHelper {

    public static JPanel createSimplePieChart(String title, Map<String, Double> data) {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw pie chart jika ada data
                if (!data.isEmpty()) {
                    drawPieChart(g2d, data);
                }
            }

            private void drawPieChart(Graphics2D g2d, Map<String, Double> data) {
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int radius = Math.min(centerX, centerY) - 50;

                double total = data.values().stream().mapToDouble(Double::doubleValue).sum();

                // Draw pie segments
                double startAngle = 0;
                int colorIndex = 0;
                Color[] colors = {
                        new Color(255, 99, 132),   // Red
                        new Color(54, 162, 235),   // Blue
                        new Color(255, 205, 86),   // Yellow
                        new Color(75, 192, 192),   // Teal
                        new Color(153, 102, 255),  // Purple
                        new Color(255, 159, 64),   // Orange
                        new Color(201, 203, 207)   // Gray
                };

                for (Map.Entry<String, Double> entry : data.entrySet()) {
                    double value = entry.getValue();
                    double angle = (value / total) * 360;

                    g2d.setColor(colors[colorIndex % colors.length]);
                    g2d.fillArc(centerX - radius, centerY - radius,
                            radius * 2, radius * 2,
                            (int) startAngle, (int) angle);

                    startAngle += angle;
                    colorIndex++;
                }

                // Draw legend
                drawLegend(g2d, data, colors);
            }

            private void drawLegend(Graphics2D g2d, Map<String, Double> data, Color[] colors) {
                int x = 20;
                int y = 20;
                int colorIndex = 0;

                for (Map.Entry<String, Double> entry : data.entrySet()) {
                    g2d.setColor(colors[colorIndex % colors.length]);
                    g2d.fillRect(x, y, 15, 15);

                    g2d.setColor(Color.BLACK);
                    g2d.drawString(String.format("%s: Rp%,.0f",
                            entry.getKey(), entry.getValue()), x + 20, y + 12);

                    y += 20;
                    colorIndex++;
                }
            }
        };

        chartPanel.setBorder(BorderFactory.createTitledBorder(title));
        chartPanel.setPreferredSize(new Dimension(400, 300));

        return chartPanel;
    }
}