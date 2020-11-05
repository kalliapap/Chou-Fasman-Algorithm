/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package choufasmanalgorithm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Kalliopi
 */
public class Diagram extends JPanel {
     private int width = 800;
    private int heigth = 400;
    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = new Color(255, 102, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(1.5f);
    private int pointWidth = 4;
    private int numberYDivisions = 10;
    private double[] scores;
    private double[] scores1;
    private String ann;
    
    public Diagram(double[] scores,double[] scores1,String ann) {
        this.scores = scores;
        this.scores1 = scores1;
        this.ann = ann;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (scores.length - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < scores.length; i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - scores[i]) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }
        List<Point> graphPoints1 = new ArrayList<>();
        for (int i = 0; i < scores1.length; i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - scores1[i]) * yScale + padding);
            graphPoints1.add(new Point(x1, y1));
        }

        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (scores.length > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < scores.length; i++) {
            if (ann.length() > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (scores.length - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                    g2.setColor(gridColor);
                    g2.setColor(Color.BLACK);
                    String xLabel = "" + ann.charAt(i);
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes 
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(new Color(255,0,0,255));
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setColor(new Color(0,0,255,255));
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints1.size() - 1; i++) {
            int x1 = graphPoints1.get(i).x;
            int y1 = graphPoints1.get(i).y;
            int x2 = graphPoints1.get(i + 1).x;
            int y2 = graphPoints1.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }
        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
        
        for (int i = 0; i < graphPoints1.size(); i++) {
            int x = graphPoints1.get(i).x - pointWidth / 2;
            int y = graphPoints1.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }

    private double getMinScore() {
        double minScore = Double.MAX_VALUE;
        for (double score : scores) {
            minScore = Math.min(minScore, score);
        }
        
        double minScore1 = Double.MAX_VALUE;
        for (double score : scores1) {
            minScore = Math.min(minScore, score);
        }
        if(minScore > minScore1){
            return minScore1;
        }else{
            return minScore;
        }
    }

    private double getMaxScore() {
        double maxScore = Double.MIN_VALUE;
        for (double score : scores) {
            maxScore = Math.max(maxScore, score);
        }
        
        double maxScore1 = Double.MIN_VALUE;
        for (double score : scores1) {
            maxScore1 = Math.max(maxScore, score);
        }
        if(maxScore > maxScore1){
            return maxScore;
        }else{
            return maxScore1;
        }
        
    }

    public void setScores(double[] scores) {
        this.scores = scores;
        invalidate();
        this.repaint();
    }

    public double[] getScores() {
        return scores;
    }

    public static void createAndShowGui(double[] scores,double[] scores1,String ann) {
      
        Diagram mainPanel = new Diagram(scores,scores1,ann);
        Border titleBorder = new TitledBorder(new LineBorder(Color.BLACK), "Chou Fasman Prediction");
         
        mainPanel.setPreferredSize(new Dimension(800, 600));
        mainPanel.setBorder(titleBorder);
        JPanel legends = new JPanel();
        
        JLabel helix = new JLabel("alpha Helix");
        JLabel sheet = new JLabel("beta Strand");
        helix.setForeground(Color.red);
        sheet.setForeground(Color.blue);

       legends.setBackground(Color.WHITE);
       legends.setBorder(new TitledBorder(new LineBorder(Color.BLACK)));
       legends.setBounds(90, 450, 100, 50);
       legends.add(helix);
       legends.add(sheet);
        JFrame frame = new JFrame("choufasman prediction");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel.setLayout(null);
        mainPanel.add(legends);
        frame.getContentPane().add(mainPanel);
       
        frame.pack();
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
       
        BufferedImage im = new BufferedImage(frame.getWidth(),frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
        frame.paint(im.getGraphics());
       
        try {
            ImageIO.write(im, "PNG", new File("choufasman.png"));
        } catch (IOException ex) {
            Logger.getLogger(Diagram.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
