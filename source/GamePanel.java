package com.dottycrumble;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.Random;
import javax.swing.JPanel;

public class GamePanel extends JPanel
{
  private Main main;
  private String gameTitle;
  private Point playButtonPosition;
  private Point exitButtonPosition;
  private Point backButtonPosition;
  private int buttonWidth;
  private int buttonHeight;
  private int buttonBorder;
  private int dotSize;
  private int dotSpace;
  private Color[] colorListTransparent;
  private Color[] colorList;
  private Point[] openingDot;
  private Color[] colorDot;

  public Point getPlayButtonPosition()
  {
    return playButtonPosition;
  }
  public Point getExitButtonPosition()
  {
    return exitButtonPosition;
  }
  public int getButtonWidth()
  {
    return buttonWidth;
  }
  public int getButtonHeight()
  {
    return buttonHeight;
  }
  public int getDotSize()
  {
    return dotSize;
  }
  public int getDotSpace()
  {
    return dotSpace;
  }
  public int getColorSize()
  {
    return colorList.length;
  }
  public GamePanel(Main main)
  {
    this.main = main;
    this.gameTitle = "DOTTY CRUMBLE";
    this.buttonWidth = 125;
    this.buttonHeight = 40;
    this.buttonBorder = 15;
    this.dotSize = 40;
    this.dotSpace = this.dotSize + 0;
    playButtonPosition = new Point();
    exitButtonPosition = new Point();
    backButtonPosition = new Point();
    colorList = new Color[10];
    colorList[0] = new Color(255, 255, 255, 255);
    colorList[1] = new Color(255, 189, 232, 255);
    colorList[2] = new Color(189, 232, 255, 255);
    colorList[3] = new Color(255, 232, 189, 255);
    colorList[4] = new Color(232, 189, 255, 255);
    colorList[5] = new Color(186, 255, 201, 255);
    colorList[6] = new Color(232, 255, 189, 255);
    colorList[7] = new Color(64, 64, 64, 255);
    colorList[8] = new Color(64, 64, 64, 255);
    colorList[9] = new Color(64, 64, 64, 255);
    colorListTransparent = new Color[10];
    colorListTransparent[0] = new Color(255, 255, 255, 150);
    colorListTransparent[1] = new Color(255, 189, 232, 150);
    colorListTransparent[2] = new Color(189, 232, 255, 150);
    colorListTransparent[3] = new Color(255, 232, 189, 150);
    colorListTransparent[4] = new Color(232, 189, 255, 150);
    colorListTransparent[5] = new Color(186, 255, 201, 150);
    colorListTransparent[6] = new Color(232, 255, 189, 150);
    colorListTransparent[7] = new Color(64, 64, 64, 255);
    colorListTransparent[8] = new Color(64, 64, 64, 255);
    colorListTransparent[9] = new Color(64, 64, 64, 255);
    initOpeningDot();
    repaint();
  }
  private void initOpeningDot()
  {
    Random random = new Random();
    openingDot = new Point[75];
    colorDot = new Color[75];
    for (int i = 0; i < openingDot.length; i++)
    {
      openingDot[i] = new Point();
      openingDot[i].x = random.nextInt(1000);
      openingDot[i].y = random.nextInt(600);
      colorDot[i] = colorList[random.nextInt(7)];
    }
  }
  public void update()
  {

  }
  public void render()
  {
    repaint();
  }
  public void paintComponent(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    Font titleFont = new Font("Atama Simple", Font.BOLD, 36);
    Font font = new Font("Atama Simple", Font.BOLD, 20);
    Font font2 = new Font("Atama Simple", Font.BOLD, 36);
    switch (main.getState())
    {
      case Main.INIT_STATE:

        clear(g2d);

        for (int i = 0; i < openingDot.length; i++)
        {
          g2d.setColor(colorDot[i]);
          g2d.fillOval(openingDot[i].x, openingDot[i].y, 15, 15);
        }

        g2d.setFont(titleFont);
        int textWidth = g.getFontMetrics().stringWidth(gameTitle);
        g2d.setColor(new Color(133, 0, 194));
        g2d.drawString(gameTitle, (Main.WIDTH - textWidth) / 2 + 15, 225);

        playButtonPosition.x = (Main.WIDTH - buttonWidth) / 2;
        playButtonPosition.y = 275;
        g2d.setColor(new Color(231, 189, 255));
        g2d.fillRoundRect(playButtonPosition.x, playButtonPosition.y, buttonWidth, buttonHeight, buttonBorder, buttonBorder);
        g2d.setFont(font);
        textWidth = g.getFontMetrics().stringWidth("PLAY");
        g2d.setColor(new Color(133, 0, 194));
        g2d.drawString("PLAY", (Main.WIDTH - textWidth) / 2, 302);

        exitButtonPosition.x = (Main.WIDTH - buttonWidth) / 2;
        exitButtonPosition.y = 325;
        g2d.setColor(new Color(231, 189, 255));
        g2d.fillRoundRect(exitButtonPosition.x, exitButtonPosition.y, buttonWidth, buttonHeight, buttonBorder, buttonBorder);
        g2d.setFont(font);
        textWidth = g.getFontMetrics().stringWidth("EXIT");
        g2d.setColor(new Color(133, 0, 194));
        g2d.drawString("EXIT", (Main.WIDTH - textWidth) / 2, 352);

        break;

      case Main.GAME_STATE:

        clear(g2d);
        for (int i = 0; i < Main.ROW; i++)
          for (int j = 0; j < Main.COLUMN; j++)
          {
            if (i == main.getHoverPoint().y && j == main.getHoverPoint().x)
              g2d.setColor(colorList[main.getColor(j, i)]);
            else
              g2d.setColor(colorListTransparent[main.getColor(j, i)]);
            if (main.getColor(j, i) >= Main.BLACK)
            {
              g2d.setColor(colorList[main.getColor(j, i)]);
              g2d.fillRoundRect(j * this.dotSpace + (Main.WIDTH - Main.COLUMN * this.dotSpace) / 2, i * this.dotSpace + 50, dotSize, dotSize, 10, 10);
            }
            else
              g2d.fillOval(j * this.dotSpace + (Main.WIDTH - Main.COLUMN * this.dotSpace) / 2, i * this.dotSpace + 50, dotSize, dotSize);
          }

        backButtonPosition.x = 75;
        backButtonPosition.y = 525;
        g2d.setColor(new Color(231, 189, 255));
        g2d.fillRoundRect(backButtonPosition.x, backButtonPosition.y, buttonWidth, buttonHeight, buttonBorder, buttonBorder);
        g2d.setFont(font);
        g2d.setColor(new Color(133, 0, 194));
        g2d.drawString("BACK", 110, 552);

        g2d.setFont(font);
        g2d.setColor(new Color(133, 0, 194));
        textWidth = g.getFontMetrics().stringWidth(main.getClickStatus());
        g2d.drawString(main.getClickStatus(), (Main.WIDTH - textWidth) / 2, 540);
        if (main.getSourcePoint() != null)
        {
          g2d.setColor(colorList[main.getColor(main.getSourcePoint().x, main.getSourcePoint().y)]);
          g2d.fillOval((Main.WIDTH - dotSize) / 2, 550, dotSize, dotSize);
        }

        g2d.setColor(new Color(231, 189, 255));
        g2d.fillRect(75, 475, Math.round(main.getLoadingPercentage() * 810), 25);


        g2d.setFont(font);
        g2d.setColor(new Color(133, 0, 194));
        g2d.drawString("Score: " + main.getScore(), 750, 540);

        break;
      case Main.GAME_OVER_STATE:

        clear(g2d);
        g2d.setFont(font2);
        g2d.setColor(new Color(133, 0, 194));
        textWidth = g.getFontMetrics().stringWidth("YOUR SCORE: " + main.getScore());
        g2d.drawString("YOUR SCORE: " + main.getScore(), (Main.WIDTH - textWidth) / 2 + 15, 250);

        backButtonPosition.x = (Main.WIDTH - buttonWidth) / 2;
        backButtonPosition.y = 300;
        g2d.setColor(new Color(231, 189, 255));
        g2d.fillRoundRect(backButtonPosition.x, backButtonPosition.y, buttonWidth, buttonHeight, buttonBorder, buttonBorder);
        g2d.setFont(font);
        g2d.setColor(new Color(133, 0, 194));
        textWidth = g.getFontMetrics().stringWidth("BACK");
        g2d.drawString("BACK", (Main.WIDTH - textWidth) / 2, 327);

        break;
    }
  }
  public void clear(Graphics2D g2d)
  {
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
  }
}